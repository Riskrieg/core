package com.riskrieg.core.internal.bundle;

import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.unsorted.gamemode.GameState;

public class UpdateBundle {

  private final Player currentTurnPlayer;
  private final GameState gameState;

  public UpdateBundle(Player currentTurnPlayer, GameState gameState) {
    this.currentTurnPlayer = currentTurnPlayer;
    this.gameState = gameState;
  }

  public GameState gameState() {
    return gameState;
  }

  public Player currentTurnPlayer() {
    return currentTurnPlayer;
  }

}
