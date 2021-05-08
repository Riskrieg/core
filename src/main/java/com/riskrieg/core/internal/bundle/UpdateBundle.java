package com.riskrieg.core.internal.bundle;

import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.internal.GameEndReason;
import java.util.Set;
import javax.annotation.Nullable;

public class UpdateBundle {

  private final Player previousPlayer;
  private final Player currentTurnPlayer;
  private final GameEndReason reason;
  private final int claims;
  private final Set<Player> defeated;

  public UpdateBundle(Player previousPlayer, Player currentTurnPlayer, GameEndReason reason, int claims, Set<Player> defeated) {
    this.previousPlayer = previousPlayer;
    this.currentTurnPlayer = currentTurnPlayer;
    this.reason = reason;
    this.claims = claims;
    this.defeated = defeated;
  }

  @Nullable
  public Player previousPlayer() {
    return previousPlayer;
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

  public Set<Player> defeated() {
    return defeated;
  }

}
