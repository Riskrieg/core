package com.riskrieg.core.internal.bundle;

import com.riskrieg.core.api.player.Player;
import javax.annotation.Nullable;

public final class SkipBundle {

  private final Player currentTurnPlayer;
  private final Player skippedPlayer;
  private final int claims;

  public SkipBundle(Player currentTurnPlayer, Player skippedPlayer, int claims) {
    this.currentTurnPlayer = currentTurnPlayer;
    this.skippedPlayer = skippedPlayer;
    this.claims = claims;
  }

  @Nullable
  public Player currentTurnPlayer() {
    return currentTurnPlayer;
  }

  public Player skippedPlayer() {
    return skippedPlayer;
  }

  public int claims() {
    return claims;
  }

}
