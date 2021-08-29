package com.riskrieg.core.internal.bundle;

import com.riskrieg.core.api.gamemode.GameState;
import com.riskrieg.core.api.player.Player;
import javax.annotation.Nullable;

public class CurrentStateBundle {

  private final Player currentTurnPlayer;
  private final GameState gameState;
  private final int claims;

  public CurrentStateBundle(Player currentTurnPlayer, GameState gameState, int claims) {
    this.currentTurnPlayer = currentTurnPlayer;
    this.gameState = gameState;
    this.claims = claims;
  }

  @Nullable
  public Player currentTurnPlayer() {
    return currentTurnPlayer;
  }

  public GameState gameState() {
    return gameState;
  }

  public int claims() {
    return claims;
  }

}
