package com.riskrieg.core.api.identifier;

import com.riskrieg.core.internal.identifier.GameIdentifierContainer;
import java.util.UUID;

public non-sealed interface GameIdentifier extends Identifier {

  static GameIdentifier of(String id) {
    return new GameIdentifierContainer(id);
  }

  static GameIdentifier uuid() {
    return new GameIdentifierContainer(UUID.randomUUID().toString());
  }

}
