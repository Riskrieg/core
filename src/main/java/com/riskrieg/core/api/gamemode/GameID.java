package com.riskrieg.core.api.gamemode;

import java.util.Objects;
import java.util.UUID;

public class GameID {

  private final String value;

  private GameID(String value) {
    Objects.requireNonNull(value);
    if (value.isBlank()) {
      throw new IllegalArgumentException("id value cannot be blank");
    }
    this.value = value;
  }

  private GameID() {
    this(UUID.randomUUID().toString());
  }

  public static GameID of(String id) {
    return new GameID(id);
  }

  public static GameID random() {
    return new GameID();
  }

  @Override
  public String toString() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameID gameID = (GameID) o;
    return value.equals(gameID.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

}
