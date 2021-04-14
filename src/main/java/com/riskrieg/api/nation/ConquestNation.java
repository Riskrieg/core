package com.riskrieg.api.nation;

import com.riskrieg.api.player.Identity;
import com.riskrieg.map.territory.Territory;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public final class ConquestNation implements Nation {

  private Identity leaderIdentity;
  private final LinkedHashSet<Territory> territories;

  public ConquestNation(Identity identity) {
    Objects.requireNonNull(identity);
//    Objects.requireNonNull(name);
//    if (name.isBlank()) {
//      throw new IllegalArgumentException("Field 'name' of type String cannot be blank");
//    }
    this.leaderIdentity = identity;
    this.territories = new LinkedHashSet<>();
  }

  @Override
  public Set<Territory> territories() {
    return Collections.unmodifiableSet(territories);
  }

  @Override
  public boolean add(Territory territory) {
    return territories.add(territory);
  }

  @Override
  public boolean remove(Territory territory) {
    return territories.remove(territory);
  }

}
