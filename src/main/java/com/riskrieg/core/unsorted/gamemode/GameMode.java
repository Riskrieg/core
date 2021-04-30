package com.riskrieg.core.unsorted.gamemode;

import com.riskrieg.core.api.Group;
import java.time.Instant;

public interface GameMode {

  Group getGroup();

  GameID getId();

  Instant creationTime();

  Instant lastUpdated();

  GameState gameState();

  void setGameState(GameState gameState);

  boolean isEnded();

}
