package com.riskrieg.core.internal.bundle;

import com.riskrieg.map.territory.TerritoryId;
import java.util.Collections;
import java.util.Set;

public final class ClaimBundle {

  private final Set<TerritoryId> claimed;
  private final Set<TerritoryId> taken;
  private final Set<TerritoryId> defended;

  public ClaimBundle(Set<TerritoryId> claimed, Set<TerritoryId> taken, Set<TerritoryId> defended) {
    this.claimed = claimed;
    this.taken = taken;
    this.defended = defended;
  }

  public Set<TerritoryId> claimed() {
    return Collections.unmodifiableSet(claimed);
  }

  public Set<TerritoryId> taken() {
    return Collections.unmodifiableSet(taken);
  }

  public Set<TerritoryId> defended() {
    return Collections.unmodifiableSet(defended);
  }

}
