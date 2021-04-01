package com.riskrieg.player;

import com.riskrieg.nation.Nation;
import java.util.Objects;

public class HumanPlayer implements Player {

  private final PlayerIdentifier identifier;
  private String name;

  public HumanPlayer(String name, PlayerColor color) {
    this.identifier = new PlayerIdentifier(color);
    Objects.requireNonNull(name);
    if (name.isBlank()) {
      throw new IllegalArgumentException("name cannot be blank");
    }
    this.name = name;
  }

  public HumanPlayer(String id, String name, PlayerColor color) {
    this.identifier = new PlayerIdentifier(id, color);
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
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HumanPlayer that = (HumanPlayer) o;
    return identifier.equals(that.identifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(identifier);
  }

}
