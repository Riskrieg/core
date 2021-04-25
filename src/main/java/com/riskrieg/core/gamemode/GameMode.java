package com.riskrieg.core.gamemode;

import java.time.Instant;

public interface GameMode {

  GameID id();

  default GameModeType type() {
    return GameModeType.UNKNOWN;
  }

  Instant creationTime();

  Instant lastUpdated();

  GameState gameState();

  void setGameState(GameState gameState);

  boolean isEnded();

}
