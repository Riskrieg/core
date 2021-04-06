package com.riskrieg.api.player;

import java.awt.Color;
import java.util.Objects;
import java.util.UUID;

public final class Player {

  private final String id;
  private final Color color;

  public Player(String id, Color color) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(color);
    if (id.isBlank()) {
      throw new IllegalArgumentException("String 'id' cannot be blank");
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

}
