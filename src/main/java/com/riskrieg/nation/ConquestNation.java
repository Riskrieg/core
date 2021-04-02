package com.riskrieg.nation;

import com.riskrieg.constant.Constants;
import com.riskrieg.gamemode.Game;
import com.riskrieg.map.GameMap;
import com.riskrieg.map.graph.Territory;
import com.riskrieg.player.Player;
import com.riskrieg.player.PlayerIdentifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class ConquestNation implements AllianceNation {

  private final PlayerIdentifier leaderIdentifier;
  private Territory capital;
  private Set<Territory> territories;
  private Set<PlayerIdentifier> allies;

  public ConquestNation(Player player, Territory capital) {
    this.leaderIdentifier = player.getIdentifier();
    this.capital = capital;
    this.territories = new HashSet<>();
    this.territories.add(capital);
    this.allies = new HashSet<>();
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
    return territories.add(territory);
  }

  @Override
  public boolean removeTerritory(Territory territory) {
    boolean isCapital = territory.equals(capital);
    boolean result = territories.remove(territory);
    if (result && isCapital) {
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
    for (Territory mTerritory : territories) {
      result.addAll(map.getNeighbors(mTerritory));
    }
    result.removeAll(territories);
    return result;
  }

  public int getClaimAmount(Game game) {
    int claims = Constants.MINIMUM_CLAIM_AMOUNT + (int) (Math.floor(territories.size() / Constants.CLAIM_INCREASE_THRESHOLD));
    return Math.min(getClaimableTerritories(game).size(), claims);
  }

  private Set<Territory> getClaimableTerritories(Game game) {
    Set<Territory> neighbors = new HashSet<>();
    Optional<GameMap> map = game.getMap();
    if (map.isPresent()) {
      neighbors = getNeighbors(map.get());
      neighbors.removeIf(territory -> {
        Optional<Nation> owner = game.getNation(territory);
        return owner.isPresent() && isAlly((AllianceNation) owner.get());
      });
    }
    return neighbors;
  }

  @Override
  public Set<PlayerIdentifier> getAllies() {
    return allies;
  }

  @Override
  public boolean isAlly(AllianceNation nation) {
    if (nation == null) {
      return false;
    }
    return allies.contains(nation.getLeaderIdentifier()) && nation.getAllies().contains(this.getLeaderIdentifier());
  }

  @Override
  public boolean addAlly(AllianceNation nation) {
    if (nation == null) {
      return false;
    }
    return allies.add(nation.getLeaderIdentifier());
  }

  @Override
  public boolean addAlly(PlayerIdentifier identifier) {
    return allies.add(identifier);
  }

  @Override
  public boolean removeAlly(AllianceNation nation) {
    if (nation == null) {
      return false;
    }
    boolean result = allies.remove(nation.getLeaderIdentifier());
    if (result && nation.getAllies().contains(this.getLeaderIdentifier())) {
      nation.removeAlly(this);
    }
    return result;
  }

  @Override
  public boolean removeAlly(PlayerIdentifier identifier) {
    return allies.remove(identifier);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConquestNation that = (ConquestNation) o;
    return leaderIdentifier.equals(that.leaderIdentifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(leaderIdentifier);
  }

}
