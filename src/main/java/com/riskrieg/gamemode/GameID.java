package com.riskrieg.gamemode;

import java.util.Objects;
import java.util.UUID;

public class GameID {

  private final String id;

  public GameID() {
    this.id = UUID.randomUUID().toString();
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
