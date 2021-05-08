package com.riskrieg.core.internal.bundle;

import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.internal.GameEndReason;
import com.riskrieg.core.unsorted.gamemode.GameState;

public class UpdateBundle {

  private final Player currentTurnPlayer;
  private final GameState gameState;
  private final GameEndReason reason;

  public UpdateBundle(Player currentTurnPlayer, GameState gameState, GameEndReason reason) {
    this.currentTurnPlayer = currentTurnPlayer;
    this.gameState = gameState;
    this.reason = reason;
  }

  public GameState gameState() {
    return gameState;
  }

  public Player currentTurnPlayer() {
    return currentTurnPlayer;
  }

  public GameEndReason reason() {
    return reason;
  }

}
