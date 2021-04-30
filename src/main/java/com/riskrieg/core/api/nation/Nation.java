package com.riskrieg.core.api.nation;

import com.riskrieg.core.api.player.Identity;
import com.riskrieg.core.unsorted.map.GameMap;
import com.riskrieg.core.unsorted.map.GameTerritory;
import com.riskrieg.core.unsorted.map.TerritoryType;
import com.riskrieg.map.territory.TerritoryId;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class Nation {

  private Identity identity;
  private final Set<GameTerritory> territories;

  public Nation(Identity identity, GameTerritory capital) {
    Objects.requireNonNull(identity);
    Objects.requireNonNull(capital);
    this.identity = identity;
    this.territories = new HashSet<>();
    this.territories.add(capital);
  }

  public Identity getLeaderIdentity() {
    return identity;
  }

  public Set<TerritoryId> territories() {
    return territories.stream().map(GameTerritory::id).collect(Collectors.toUnmodifiableSet());
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

  public boolean isOfType(TerritoryId id, TerritoryType type) {
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

  // TODO: If capital is removed, select a new capital
  public boolean remove(GameTerritory territory) {
    return territories.remove(territory);
  }

  public boolean remove(TerritoryId id) {
    return territories.removeIf(gt -> gt.id().equals(id));
  }

}
