package com.riskrieg.core.api.game.map;

import com.riskrieg.core.api.game.map.territory.Nucleus;
import com.riskrieg.core.api.identifier.TerritoryIdentifier;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public record Territory(String id, Set<Nucleus> nuclei) implements TerritoryIdentifier {

  public Territory {
    Objects.requireNonNull(id);
    Objects.requireNonNull(nuclei);
    if (id.isBlank()) {
      throw new IllegalStateException("String 'id' cannot be blank");
    }
    if (nuclei.isEmpty()) {
      throw new IllegalStateException("Set<Nucleus> 'nuclei' must not be empty");
    }
  }

  public Territory(String id, Nucleus nucleus) {
    this(id, Collections.singleton(nucleus));
  }

  public String name() {
    return id;
  }

}
