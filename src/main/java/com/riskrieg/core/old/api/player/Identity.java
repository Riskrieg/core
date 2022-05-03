/*
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

package com.riskrieg.core.old.api.player;

import java.util.Objects;
import java.util.UUID;

public final class Identity {

  private final String id;

  private Identity(String id) {
    Objects.requireNonNull(id);
    if (id.isBlank()) {
      throw new IllegalArgumentException("String 'id' cannot be blank");
    }
    this.id = id;
  }

  private Identity() {
    this(UUID.randomUUID().toString());
  }

  public static Identity of(String id) {
    return new Identity(id);
  }

  public static Identity random() {
    return new Identity();
  }

  @Override
  public String toString() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Identity identity = (Identity) o;
    return id.equals(identity.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

}
