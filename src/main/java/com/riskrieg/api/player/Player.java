package com.riskrieg.api.player;

import java.awt.Color;
import java.util.Objects;

public final class Player {

  private final Color color;
  private final Identity identity;

  public Player(Identity identity, Color color) {
    Objects.requireNonNull(color);
    this.identity = identity;
    this.color = color;
  }

  public Player(Color color) {
    this(new Identity(), color);
  }

  public Identity identity() {
    return identity;
  }

  public Color color() {
    return color;
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
