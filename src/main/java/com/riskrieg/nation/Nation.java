package com.riskrieg.nation;

import com.riskrieg.player.Identity;
import com.riskrieg.map.GameTerritory;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class Nation {

  private Identity leaderIdentity;
  private final Set<GameTerritory> territories;

  public Nation(Identity identity, GameTerritory capital) {
    Objects.requireNonNull(identity);
    Objects.requireNonNull(capital);
    this.leaderIdentity = identity;
    this.territories = new HashSet<>();
    this.territories.add(capital);
  }

  public Identity getLeaderIdentity() {
    return leaderIdentity;
  }

  public Set<GameTerritory> territories() {
    return Collections.unmodifiableSet(territories);
  }

  public boolean add(GameTerritory territory) {
    return territories.add(territory);
  }

  public boolean remove(GameTerritory territory) {
    return territories.remove(territory);
  }

}
