package com.riskrieg.core.internal.bundle;

import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.internal.GameEndReason;
import javax.annotation.Nullable;

public class UpdateBundle {

  private final Player currentTurnPlayer;
  private final GameEndReason reason;
  private final int claims;

  public UpdateBundle(Player currentTurnPlayer, GameEndReason reason, int claims) {
    this.currentTurnPlayer = currentTurnPlayer;
    this.reason = reason;
    this.claims = claims;
  }

  @Nullable
  public Player currentTurnPlayer() {
    return currentTurnPlayer;
  }

  public GameEndReason reason() {
    return reason;
  }

  public int claims() {
    return claims;
  }

}
