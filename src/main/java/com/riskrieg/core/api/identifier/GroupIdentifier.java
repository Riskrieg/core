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

package com.riskrieg.core.api.identifier;

import java.util.Objects;
import java.util.UUID;

public record GroupIdentifier(String id) implements Identifier {

  public GroupIdentifier {
    Objects.requireNonNull(id);
    if (id.isBlank()) {
      throw new IllegalStateException("String 'id' cannot be blank");
    }
  }

  public static GroupIdentifier of(String id) {
    return new GroupIdentifier(id);
  }

  public static GroupIdentifier uuid() {
    return new GroupIdentifier(UUID.randomUUID().toString());
  }

}
