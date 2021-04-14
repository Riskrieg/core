package com.riskrieg.api.player;

import java.awt.Color;
import java.util.Objects;

public final class Player {

  private final Color color;
  private final Identity identity;
  private String name;

  public Player(Identity identity, Color color, String name) {
    Objects.requireNonNull(color);
    this.identity = identity;
    this.color = color;
    this.name = name;
  }

  public Player(Color color, String name) {
    this(new Identity(), color, name);
  }

  public Identity identity() {
    return identity;
  }

  public Color color() {
    return color;
  }

  public String name() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Player player = (Player) o;
    return color.equals(player.color);
  }

  @Override
  public int hashCode() {
    return Objects.hash(color);
  }

}
