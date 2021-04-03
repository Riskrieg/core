package com.riskrieg.api;

import com.riskrieg.gamemode.Game;

public class Save { // TODO: Convert to record after Gson/Moshi add record support

  private final Game game;

  public Save(Game game) {
    this.game = game;
  }

  public Game game() {
    return game;
  }

}
