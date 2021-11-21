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

package com.riskrieg.core.api.map;

import com.riskrieg.map.territory.TerritoryId;
import java.util.Objects;

public final class GameTerritory {

  private final TerritoryId id;
  private final TerritoryType type;

  public GameTerritory(TerritoryId id, TerritoryType type) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(type);
    this.id = id;
    this.type = type;
  }

  public GameTerritory(TerritoryId id) {
    this(id, TerritoryType.NORMAL);
  }

  public TerritoryId id() {
    return id;
  }

  public TerritoryType type() {
    return type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameTerritory that = (GameTerritory) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

}
