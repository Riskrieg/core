package com.riskrieg.core.api.identifier;

import com.riskrieg.core.internal.identifier.TerritoryIdentifierContainer;

public non-sealed interface TerritoryIdentifier extends Identifier {

  static TerritoryIdentifier of(String id) {
    return new TerritoryIdentifierContainer(id);
  }

}
