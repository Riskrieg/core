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

package com.riskrieg.core.api.game.territory;

import com.riskrieg.core.api.identifier.TerritoryIdentifier;
import java.util.Objects;

public record GameTerritory(TerritoryIdentifier identifier, TerritoryType type) {

  public static GameTerritory of(TerritoryIdentifier identifier, TerritoryType type) {
    return new GameTerritory(identifier, type);
  }

  public static GameTerritory of(TerritoryIdentifier identifier) {
    return new GameTerritory(identifier, TerritoryType.PLAIN);
  }

  public GameTerritory {
    Objects.requireNonNull(identifier);
    Objects.requireNonNull(type);
  }

  public GameTerritory(TerritoryIdentifier identifier) {
    this(identifier, TerritoryType.PLAIN);
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
    return identifier.equals(that.identifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(identifier);
  }

}
