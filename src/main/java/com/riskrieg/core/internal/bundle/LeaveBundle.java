package com.riskrieg.core.internal.bundle;

import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.unsorted.gamemode.GameState;

public final class LeaveBundle {

  private final Player leavingPlayer;
  private final GameState gameState;

  public LeaveBundle(Player leavingPlayer, GameState gameState) {
    this.leavingPlayer = leavingPlayer;
    this.gameState = gameState;
  }

  public GameState gameState() {
    return gameState;
  }

  public Player leavingPlayer() {
    return leavingPlayer;
  }

}
