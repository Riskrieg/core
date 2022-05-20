/*
 *     Riskrieg, an open-source conflict simulation game.
 *     Copyright (C) 2021 Aaron Yoder <aaronjyoder@gmail.com> and Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.riskrieg.core.internal;

import com.riskrieg.core.api.Riskrieg;
import com.riskrieg.core.api.group.Group;
import com.riskrieg.core.api.identifier.GroupIdentifier;
import com.riskrieg.core.api.requests.GameAction;
import com.riskrieg.core.internal.group.LocalGroup;
import com.riskrieg.core.internal.requests.GenericAction;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public record LocalRiskrieg(Path repository) implements Riskrieg {

  public LocalRiskrieg {
    Objects.requireNonNull(repository);
    if (!Files.isDirectory(repository)) {
      throw new IllegalStateException("The repository path provided must be a directory/folder.");
    }
  }

  @NonNull
  @Override
  public GameAction<Group> createGroup(GroupIdentifier identifier) {
    Path groupPath = repository.resolve(identifier.id());
    try {
      Files.createDirectories(groupPath);
      return new GenericAction<>(new LocalGroup(groupPath));
    } catch (IOException e) {
      return new GenericAction<>(e);
    }
  }

  @NonNull
  @Override
  public GameAction<Group> retrieveGroup(GroupIdentifier identifier) {
    Path groupPath = repository.resolve(identifier.id());
    if (Files.exists(groupPath)) {
      return new GenericAction<>(new LocalGroup(groupPath));
    }
    return new GenericAction<>(new NoSuchFileException("The group with the specified identifier does not exist"));
  }

  @NonNull
  @Override
  public GameAction<Collection<Group>> retrieveAllGroups() {
    try (var elements = Files.list(repository)) {
      List<Group> result = elements.filter(Files::isDirectory)
          .map(LocalGroup::new)
          .map(Group.class::cast)
          .toList();
      return new GenericAction<>(result);
    } catch (IOException e) {
      return new GenericAction<>(e);
    }
  }

  @NonNull
  @Override
  public GameAction<Boolean> deleteGroup(GroupIdentifier identifier) {
    Path groupPath = repository.resolve(identifier.id());
    try (var paths = Files.walk(groupPath)) { // Delete everything within the group first.
      final List<Path> pathsToDelete = paths.sorted(Comparator.reverseOrder()).toList();
      for (Path path : pathsToDelete) { // Doing it this way in order to catch the exception more easily.
        Files.deleteIfExists(path);
      }
      return new GenericAction<>(Files.deleteIfExists(groupPath)); // Delete the group
    } catch (IOException e) {
      return new GenericAction<>(false, e);
    }
  }

}
