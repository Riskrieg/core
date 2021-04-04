package com.riskrieg.player;

import java.util.Objects;
import java.util.UUID;

public class PlayerIdentifier { // TODO: Convert to record after Gson/Moshi add record support

  private final String id;
  private final PlayerColor color;

  public PlayerIdentifier(String id, PlayerColor color) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(color);
    if (id.isBlank()) {
      throw new IllegalArgumentException("id cannot be blank");
    }
    this.id = id;
    this.color = color;
  }

  public PlayerIdentifier(PlayerColor color) {
    Objects.requireNonNull(color);
    this.id = UUID.randomUUID().toString();
    this.color = color;
  }

  public String id() {
    return id;
  }

  public PlayerColor color() {
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
    PlayerIdentifier that = (PlayerIdentifier) o;
    return id.equals(that.id) && color == that.color;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, color);
  }

}
