package com.riskrieg.player;

import com.riskrieg.map.GameMap;
import com.riskrieg.map.graph.Territory;
import com.riskrieg.nation.Nation;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class ComputerPlayer implements Player {

  private final PlayerIdentifier identifier;
  private String name;

  public ComputerPlayer(String name, PlayerColor color) {
    this.identifier = new PlayerIdentifier(color);
    Objects.requireNonNull(name);
    if (name.isBlank()) {
      throw new IllegalArgumentException("name cannot be blank");
    }
    this.name = name;
  }

  @Override
  public PlayerIdentifier getIdentifier() {
    return identifier;
  }

  @Override
  public String getID() {
    return identifier.id();
  }

  @Override
  public PlayerColor getColor() {
    return identifier.color();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    Objects.requireNonNull(name);
    this.name = name;
  }

  @Override
  public boolean isLeader(Nation nation) {
    return this.identifier.equals(nation.getLeaderIdentifier());
  }

  @Override
  public boolean isComputer() {
    return true;
  }


  /**
   * This method creates a list of territories not already taken by another nation and then selects one of them if present. It will first attempt to create a list of territories
   * such that any available territory is not directly next to an already-taken territory. If that is not possible, it will include the set of territories that neighbor
   * already-taken territories and select from that.
   *
   * @param gameMap The game map.
   * @param nations The set of all nations.
   * @return An available territory, if present, otherwise, an empty Optional.
   */
  public Optional<Territory> selectCapital(GameMap gameMap, Set<Nation> nations) {
    Set<Territory> available = new LinkedHashSet<>(gameMap.getTerritories());
    Set<Territory> taken = new LinkedHashSet<>();
    Set<Territory> neighborsOfTaken = new LinkedHashSet<>();

    for (Nation nation : nations) {
      taken.addAll(nation.getTerritories());
      neighborsOfTaken.addAll(nation.getNeighbors(gameMap));
    }

    available.removeAll(taken);
    available.removeAll(neighborsOfTaken);
    if (available.isEmpty()) {
      available.addAll(neighborsOfTaken);
      available.removeAll(taken);
    }

    if (available.isEmpty()) {
      return Optional.empty();
    }
    List<Territory> result = new LinkedList<>(available);
    Collections.shuffle(result);
    return Optional.of(result.get(0));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ComputerPlayer that = (ComputerPlayer) o;
    return identifier.equals(that.identifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(identifier);
  }

}
