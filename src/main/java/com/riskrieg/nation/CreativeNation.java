package com.riskrieg.nation;

import com.riskrieg.map.GameMap;
import com.riskrieg.map.graph.Territory;
import com.riskrieg.player.Player;
import com.riskrieg.player.PlayerIdentifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class CreativeNation implements Nation {

  private final PlayerIdentifier leaderIdentifier;
  private Territory capital;
  private Set<Territory> territories;

  public CreativeNation(Player player, Territory capital) {
    this.leaderIdentifier = player.getIdentifier();
    this.capital = capital;
    this.territories = new HashSet<>();
    this.territories.add(capital);
  }

  @Override
  public PlayerIdentifier getLeaderIdentifier() {
    return leaderIdentifier;
  }

  @Override
  public Territory getCapital() {
    return capital;
  }

  @Override
  public Set<Territory> getTerritories() {
    return territories;
  }

  @Override
  public boolean addTerritory(Territory territory) {
    if (capital == null) {
      capital = territory;
    }
    return territories.add(territory);
  }

  @Override
  public boolean removeTerritory(Territory territory) {
    boolean result = territories.remove(territory);
    if (result && territory.equals(capital)) {
      if (territories.size() > 0) {
        List<Territory> potentialCapitals = new ArrayList<>(territories);
        Collections.shuffle(potentialCapitals);
        capital = potentialCapitals.get(0);
      } else {
        capital = null;
      }
    }
    return result;
  }

  @Override
  public Set<Territory> getNeighbors(GameMap map) {
    Set<Territory> result = new HashSet<>();
    if (map == null) {
      return result;
    }
    for (Territory territory : map.getTerritories()) {
      for (Territory mTerritory : territories) {
        if (map.neighbors(mTerritory, territory) && !territories.contains(territory)) {
          result.add(territory);
        }
      }
    }
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreativeNation that = (CreativeNation) o;
    return leaderIdentifier.equals(that.leaderIdentifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(leaderIdentifier);
  }

}
