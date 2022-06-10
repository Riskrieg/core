package com.riskrieg.core.api.game.entity.alliance;

import com.riskrieg.core.api.identifier.NationIdentifier;
import java.util.Objects;

public record Alliance(NationIdentifier ally, NationIdentifier coally) {

  public Alliance {
    Objects.requireNonNull(ally);
    Objects.requireNonNull(coally);
    if (ally == coally) {
      throw new IllegalStateException("You cannot be in an alliance with yourself");
    }
  }

}
