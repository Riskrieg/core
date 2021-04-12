package com.riskrieg.api.player;

import java.awt.Color;
import java.util.Objects;
import java.util.UUID;

public final class Player {

  private final Color color;
  private final String id;

  public Player(String id, Color color) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(color);
    if (id.isBlank()) {
      throw new IllegalArgumentException("Field 'id' of type String cannot be blank");
    }
    this.id = id;
    this.color = color;
  }

  public Player(Color color) {
    this(UUID.randomUUID().toString(), color);
  }

  public String id() {
    return id;
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
