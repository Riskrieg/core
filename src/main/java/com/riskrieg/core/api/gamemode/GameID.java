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

package com.riskrieg.core.api.gamemode;

import java.util.Objects;
import java.util.UUID;

public final class GameID {

  private final String value;

  private GameID(String value) {
    Objects.requireNonNull(value);
    if (value.isBlank()) {
      throw new IllegalArgumentException("id value cannot be blank");
    }
    this.value = value;
  }

  private GameID() {
    this(UUID.randomUUID().toString());
  }

  public static GameID of(String id) {
    return new GameID(id);
  }

  public static GameID random() {
    return new GameID();
  }

  @Override
  public String toString() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameID gameID = (GameID) o;
    return value.equals(gameID.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

}
