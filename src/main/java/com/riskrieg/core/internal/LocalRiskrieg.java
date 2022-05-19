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
import edu.umd.cs.findbugs.annotations.NonNull;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;

public record LocalRiskrieg(Path path) implements Riskrieg { // TODO: Implement

  public LocalRiskrieg {
    Objects.requireNonNull(path);
    if (!Files.isDirectory(path)) {
      throw new IllegalStateException("The path provided must be a directory/folder.");
    }
  }

  @NonNull
  @Override
  public GameAction<Group> createGroup(GroupIdentifier identifier) {
    return null;
  }

  @NonNull
  @Override
  public GameAction<Group> retrieveGroup(GroupIdentifier identifier) {
    return null;
  }

  @NonNull
  @Override
  public GameAction<Collection<Group>> retrieveAllGroups() {
    return null;
  }

  @NonNull
  @Override
  public GameAction<Boolean> deleteGroup(GroupIdentifier identifier) {
    return null;
  }

}
