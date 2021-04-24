package com.riskrieg.core.gamemode;

public interface GameMode {

  GameID id();

  Moment creationTime();

  Moment lastUpdated();

  GameState gameState();

  void setGameState(GameState gameState);

  boolean isEnded();

}