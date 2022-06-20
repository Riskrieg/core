package com.riskrieg.core.internal.legacy.player;

import com.riskrieg.core.api.game.entity.player.Player;
import com.riskrieg.core.api.identifier.PlayerIdentifier;
import com.riskrieg.core.internal.legacy.LegacyIdentity;

public record LegacyPlayer(LegacyIdentity identity, LegacyColorId colorId, String name) {

  public Player toPlayer() {
    return new Player(PlayerIdentifier.of(identity.id()), name);
  }

}
