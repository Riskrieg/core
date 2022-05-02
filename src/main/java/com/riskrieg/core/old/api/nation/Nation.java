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

package com.riskrieg.core.old.api.nation;

import com.riskrieg.core.old.api.map.GameMap;
import com.riskrieg.core.old.api.map.GameTerritory;
import com.riskrieg.core.old.api.map.TerritoryType;
import com.riskrieg.core.old.api.player.Identity;
import com.riskrieg.core.old.constant.Constants;
import com.riskrieg.map.territory.TerritoryId;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class Nation {

  private Identity identity;
  private final Set<GameTerritory> territories;
  private final Set<Identity> allies;

  public Nation(Identity identity, GameTerritory capital) {
    Objects.requireNonNull(identity);
    Objects.requireNonNull(capital);
    this.identity = identity;
    this.territories = new HashSet<>();
    this.territories.add(capital);
    this.allies = new HashSet<>();
  }

  public Identity identity() {
    return identity;
  }

  public Set<TerritoryId> territories() {
    return territories.stream().map(GameTerritory::id).collect(Collectors.toUnmodifiableSet());
  }

  public Set<Identity> allies() {
    return Collections.unmodifiableSet(allies);
  }

  public Set<TerritoryId> neighbors(GameMap gameMap) {
    Set<TerritoryId> neighbors = new HashSet<>();
    var ids = territories();
    for (TerritoryId id : ids) {
      neighbors.addAll(gameMap.getNeighbors(id));
    }
    neighbors.removeAll(ids);
    return neighbors;
  }

  public int getClaimAmount(GameMap gameMap, Collection<Nation> nations) {
    int claims = Constants.MINIMUM_CLAIM_AMOUNT + (int) (Math.floor(territories.size() / Constants.CLAIM_INCREASE_THRESHOLD));
    return Math.min(getClaimableTerritories(gameMap, nations).size(), claims);
  }

  private Set<TerritoryId> getClaimableTerritories(GameMap gameMap, Collection<Nation> nations) {
    Set<TerritoryId> neighbors = new HashSet<>(neighbors(gameMap));
    Set<Nation> allies = nations.stream().filter(n -> this.isAllied(n.identity()) && n.isAllied(this.identity())).collect(Collectors.toSet());
    neighbors.removeIf(territoryId -> allies.stream().anyMatch(n -> n.territories().contains(territoryId)));
    return Collections.unmodifiableSet(neighbors);
  }

  public boolean territoryIsOfType(TerritoryId id, TerritoryType type) {
    GameTerritory gameTerritory = territories.stream().filter(gt -> gt.id().equals(id)).findAny().orElse(null);
    return gameTerritory != null && gameTerritory.type().equals(type);
  }

  public boolean add(GameTerritory territory) {
    return territories.add(territory);
  }

  public boolean add(TerritoryId id) {
    return territories.add(new GameTerritory(id));
  }

  public boolean add(TerritoryId id, TerritoryType type) {
    return territories.add(new GameTerritory(id, type));
  }

  public boolean remove(GameTerritory territory) {
    return territories.remove(territory);
  }

  public boolean remove(TerritoryId id) {
    return territories.removeIf(gt -> gt.id().equals(id));
  }

  public boolean isAllied(Identity identity) {
    return allies.contains(identity);
  }

  public boolean addAlly(Identity identity) {
    if (this.identity.equals(identity)) {
      return false;
    }
    return allies.add(identity);
  }

  public boolean removeAlly(Identity identity) {
    return allies.remove(identity);
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Nation nation = (Nation) o;
    return identity.equals(nation.identity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(identity);
  }

}