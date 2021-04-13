package com.riskrieg.api.nation;

import com.riskrieg.map.territory.Territory;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public final class Nation {

  private String name;
  private final LinkedHashSet<Territory> territories;

  public Nation(String name) {
    this.name = name;
    this.territories = new LinkedHashSet<>();
  }

  public String name() {
    return name;
  }

  public Set<Territory> territories() {
    return Collections.unmodifiableSet(territories);
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean add(Territory territory) {
    return territories.add(territory);
  }

  public boolean remove(Territory territory) {
    return territories.remove(territory);
  }

}
