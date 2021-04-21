package com.riskrieg.nation;

import com.riskrieg.map.GameTerritory;
import com.riskrieg.player.Identity;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class Nation {

  private Identity identity;
  private final Set<GameTerritory> territories;

  public Nation(Identity identity, GameTerritory capital) {
    Objects.requireNonNull(identity);
    Objects.requireNonNull(capital);
    this.identity = identity;
    this.territories = new HashSet<>();
    this.territories.add(capital);
  }

  public Identity getLeaderIdentity() {
    return identity;
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
