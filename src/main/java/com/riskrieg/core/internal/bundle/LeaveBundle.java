package com.riskrieg.core.internal.bundle;

import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.internal.GameEndReason;
import com.riskrieg.core.unsorted.gamemode.GameState;

public final class LeaveBundle {

  private final Player leavingPlayer;
  private final GameState gameState;
  private final GameEndReason reason;

  public LeaveBundle(Player leavingPlayer, GameState gameState, GameEndReason reason) {
    this.leavingPlayer = leavingPlayer;
    this.gameState = gameState;
    this.reason = reason;
  }

  public GameState gameState() {
    return gameState;
  }

  public Player leavingPlayer() {
    return leavingPlayer;
  }

  public GameEndReason reason() {
    return reason;
  }

}
