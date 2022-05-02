package com.riskrieg.core.api.identifier;

import com.riskrieg.core.internal.identifier.CustomPlayerIdentifier;
import java.util.UUID;

public non-sealed interface PlayerIdentifier extends Identifier {

  static PlayerIdentifier of(String id) {
    return new CustomPlayerIdentifier(id);
  }

  static PlayerIdentifier uuid() {
    return new CustomPlayerIdentifier(UUID.randomUUID().toString());
  }

}
