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

import com.riskrieg.map.territory.TerritoryIdentity;
import java.util.Objects;

public record GameTerritory(TerritoryIdentity identity, TerritoryType type) {

  public static GameTerritory of(TerritoryIdentity identity, TerritoryType type) {
    return new GameTerritory(identity, type);
  }

  public static GameTerritory of(TerritoryIdentity identity) {
    return new GameTerritory(identity, TerritoryType.PLAIN);
  }

  public GameTerritory {
    Objects.requireNonNull(identity);
    Objects.requireNonNull(type);
  }

  public GameTerritory(TerritoryIdentity identity) {
    this(identity, TerritoryType.PLAIN);
  }

  public GameTerritory with(TerritoryType type) {
    return new GameTerritory(identity, type);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameTerritory gt = (GameTerritory) o;
    return identity.equals(gt.identity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(identity);
  }

}
