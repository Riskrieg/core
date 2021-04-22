package com.riskrieg.gamemode;

public interface GameMode {

  GameID id();

  Moment creationTime();

  Moment lastUpdated();

  GameState gameState();

}
