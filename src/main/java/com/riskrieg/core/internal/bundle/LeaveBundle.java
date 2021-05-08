package com.riskrieg.core.internal.bundle;

import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.internal.GameEndReason;
import javax.annotation.Nullable;

public final class LeaveBundle {

  private final Player currentTurnPlayer;
  private final Player leavingPlayer;
  private final GameEndReason reason;
  private final int claims;

  public LeaveBundle(Player currentTurnPlayer, Player leavingPlayer, GameEndReason reason, int claims) {
    this.currentTurnPlayer = currentTurnPlayer;
    this.leavingPlayer = leavingPlayer;
    this.reason = reason;
    this.claims = claims;
  }

  @Nullable
  public Player currentTurnPlayer() {
    return currentTurnPlayer;
  }

  public Player leavingPlayer() {
    return leavingPlayer;
  }

  public GameEndReason reason() {
    return reason;
  }

  public int claims() {
    return claims;
  }

}
