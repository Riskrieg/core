package com.riskrieg.core.api.identifier;

import com.riskrieg.core.internal.identifier.PlayerIdentifierContainer;
import java.util.UUID;

public non-sealed interface PlayerIdentifier extends Identifier {

  static PlayerIdentifier of(String id) {
    return new PlayerIdentifierContainer(id);
  }

  static PlayerIdentifier uuid() {
    return new PlayerIdentifierContainer(UUID.randomUUID().toString());
  }

}
