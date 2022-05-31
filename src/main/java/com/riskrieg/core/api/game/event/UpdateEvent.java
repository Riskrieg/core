package com.riskrieg.core.api.game.event;

import com.riskrieg.core.api.game.EndReason;
import com.riskrieg.core.api.game.entity.player.Player;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public record UpdateEvent(Optional<Player> currentPlayer, Optional<Player> previousPlayer, Set<Player> defeatedPlayers, EndReason endReason) {

  public UpdateEvent {
    Objects.requireNonNull(currentPlayer);
    Objects.requireNonNull(previousPlayer);
    Objects.requireNonNull(defeatedPlayers);
    Objects.requireNonNull(endReason);
    defeatedPlayers = Set.copyOf(defeatedPlayers);
  }

}
