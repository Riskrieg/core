/**
 *     Riskrieg, an open-source conflict simulation game.
 *     Copyright (C) 2021  Aaron Yoder <aaronjyoder@gmail.com>
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

import com.riskrieg.core.api.Group;
import com.riskrieg.core.api.Riskrieg;
import com.riskrieg.core.internal.action.CompletableAction;
import com.riskrieg.core.internal.action.GenericAction;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.annotation.Nonnull;

public final class FileFolderRiskrieg implements Riskrieg {

  private final Path repository;

  public FileFolderRiskrieg(Path repository) {
    this.repository = repository;
  }

  public Path getRepository() {
    return repository;
  }

  @Nonnull
  @Override
  public CompletableAction<Group> createGroup(@Nonnull String id) {
    try {
      Path groupPath = repository.resolve(id);
      Files.createDirectories(groupPath);
      return new GenericAction<>(new FileFolderGroup(groupPath));
    } catch (Exception e) {
      return new GenericAction<>(e);
    }
  }

  @Nonnull
  @Override
  public CompletableAction<Group> retrieveGroupById(@Nonnull String id) {
    try {
      Path groupPath = repository.resolve(id);
      if (!Files.exists(groupPath)) {
        throw new FileNotFoundException("No saves exist");
      }
      return new GenericAction<>(new FileFolderGroup(groupPath));
    } catch (Exception e) {
      return new GenericAction<>(e);
    }
  }

}
