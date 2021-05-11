package com.riskrieg.core.internal.bundle;

import com.riskrieg.core.api.player.Player;

public final class AllianceBundle {

  private final Player player1;
  private final Player player2;

  public AllianceBundle(Player player1, Player player2) {
    this.player1 = player1;
    this.player2 = player2;
  }

  public Player player1() {
    return player1;
  }

  public Player player2() {
    return player2;
  }

}
