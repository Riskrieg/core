package com.riskrieg.core.gamemode;

import java.util.Objects;
import java.util.UUID;

public class GameID {

  private final String id;

  private GameID(String id) {
    Objects.requireNonNull(id);
    if (id.isBlank()) {
      throw new IllegalArgumentException("id cannot be blank");
    }
    this.id = id;
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
    return id;
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
    return id.equals(gameID.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

}
