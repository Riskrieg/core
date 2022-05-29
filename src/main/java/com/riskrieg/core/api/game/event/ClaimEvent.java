package com.riskrieg.core.api.game.event;

import com.riskrieg.core.api.game.territory.Claim;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public record ClaimEvent(Set<Claim> freeClaims, Set<Claim> wonClaims, Set<Claim> defendedClaims) {

  public ClaimEvent {
    Objects.requireNonNull(freeClaims);
    Objects.requireNonNull(wonClaims);
    Objects.requireNonNull(defendedClaims);
    freeClaims = Set.copyOf(freeClaims);
    wonClaims = Set.copyOf(wonClaims);
    defendedClaims = Set.copyOf(defendedClaims);
  }

  public ClaimEvent(Set<Claim> freeClaims) {
    this(freeClaims, Collections.emptySet(), Collections.emptySet());
  }

}
