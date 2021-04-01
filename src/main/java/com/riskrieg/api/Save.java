package com.riskrieg.api;

import com.riskrieg.gamemode.Game;

public class Save { // TODO: Convert to Record with Java 16

  private final Game game;

  public Save(Game game) {
    this.game = game;
  }

  public Game game() {
    return game;
  }

}
