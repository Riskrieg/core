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

package com.riskrieg.core.api;

import com.riskrieg.core.internal.LocalRiskrieg;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class RiskriegBuilder {

  private final Path repository;

  private RiskriegBuilder(Path repository) {
    Objects.requireNonNull(repository);
    if (!Files.isDirectory(repository)) {
      throw new IllegalStateException("The repository repository provided must be a directory/folder.");
    }
    this.repository = repository;
  }

  public static RiskriegBuilder createLocal(@NonNull Path repository) {
    return new RiskriegBuilder(repository);
  }

  private static RiskriegBuilder createRemote() { // TODO: Implement support for remote repositories accessible via a REST API.
    return null;
  }

  public Riskrieg build() { // This is set up the way it is in preparation for allowing remote repositories.
    if (repository != null) {
      return new LocalRiskrieg(repository);
    }
    return new LocalRiskrieg(null);
  }

}
