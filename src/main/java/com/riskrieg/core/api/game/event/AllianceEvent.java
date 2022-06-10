package com.riskrieg.core.api.game.event;

import com.riskrieg.core.api.game.EndReason;
import com.riskrieg.core.api.game.entity.nation.Nation;
import com.riskrieg.core.api.game.entity.player.Player;
import com.riskrieg.core.api.game.feature.alliance.AllianceStatus;
import java.util.Objects;

public record AllianceEvent(Nation ally, Player allyLeader, Nation coally, Player coallyLeader, AllianceStatus status, EndReason reason) {

  public AllianceEvent {
    Objects.requireNonNull(ally);
    Objects.requireNonNull(allyLeader);
    Objects.requireNonNull(coally);
    Objects.requireNonNull(coallyLeader);
    Objects.requireNonNull(status);
    Objects.requireNonNull(reason);
  }

}
