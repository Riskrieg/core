package com.riskrieg.core.api.game.event;

import com.riskrieg.core.api.game.entity.nation.Nation;
import com.riskrieg.core.api.game.entity.player.Player;
import com.riskrieg.core.api.game.territory.Claim;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public record ClaimEvent(Nation attacker, Player leader, Set<Claim> freeClaims, Set<Claim> wonClaims, Set<Claim> defendedClaims) {

  public ClaimEvent {
    Objects.requireNonNull(attacker);
    Objects.requireNonNull(leader);
    Objects.requireNonNull(freeClaims);
    Objects.requireNonNull(wonClaims);
    Objects.requireNonNull(defendedClaims);
    freeClaims = Set.copyOf(freeClaims);
    wonClaims = Set.copyOf(wonClaims);
    defendedClaims = Set.copyOf(defendedClaims);
  }

  public ClaimEvent(Nation attacker, Player leader, Set<Claim> freeClaims) {
    this(attacker, leader, freeClaims, Collections.emptySet(), Collections.emptySet());
  }

}
