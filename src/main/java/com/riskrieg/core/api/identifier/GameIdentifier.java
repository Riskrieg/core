package com.riskrieg.core.api.identifier;

import com.riskrieg.core.internal.identifier.CustomGameIdentifier;
import java.util.UUID;

public non-sealed interface GameIdentifier extends Identifier {

  static GameIdentifier of(String id) {
    return new CustomGameIdentifier(id);
  }

  static GameIdentifier uuid() {
    return new CustomGameIdentifier(UUID.randomUUID().toString());
  }

}
