package com.riskrieg.core.internal.identifier;

import com.riskrieg.core.api.identifier.TerritoryIdentifier;
import java.util.Objects;

public record TerritoryIdentifierContainer(String id) implements TerritoryIdentifier {

  public TerritoryIdentifierContainer {
    Objects.requireNonNull(id);
    if (id.isBlank()) {
      throw new IllegalArgumentException("String 'id' cannot be blank");
    }
  }

}
