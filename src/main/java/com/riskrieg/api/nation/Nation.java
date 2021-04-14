package com.riskrieg.api.nation;

import com.riskrieg.api.player.Identity;
import com.riskrieg.map.territory.Territory;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class Nation {

  private Identity leaderIdentity;
  private final Set<Territory> territories;

  public Nation(Identity identity, Territory capital) {
    Objects.requireNonNull(identity);
    Objects.requireNonNull(capital);
    this.leaderIdentity = identity;
    this.territories = new HashSet<>();
    this.territories.add(capital);
  }

  public Identity getLeaderIdentity() {
    return leaderIdentity;
  }

  public Set<Territory> territories() {
    return Collections.unmodifiableSet(territories);
  }

  public boolean add(Territory territory) {
    return territories.add(territory);
  }

  public boolean remove(Territory territory) {
    return territories.remove(territory);
  }

}
