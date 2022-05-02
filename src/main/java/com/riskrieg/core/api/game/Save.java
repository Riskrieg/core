package com.riskrieg.core.api.game;

import java.time.Instant;

public record Save(String id, GameMode mode, Instant creationTime, Instant updatedTime) {

  public static final String FILE_EXT = ".json";

  public Save(Game game) {
    this(game.id(), game.mode(), game.creationTime(), game.updatedTime());
  }

}
