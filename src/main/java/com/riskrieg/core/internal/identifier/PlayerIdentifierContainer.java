package com.riskrieg.core.internal.identifier;

import com.riskrieg.core.api.identifier.PlayerIdentifier;
import java.util.Objects;

public record PlayerIdentifierContainer(String id) implements PlayerIdentifier {

  public PlayerIdentifierContainer {
    Objects.requireNonNull(id);
    if (id.isBlank()) {
      throw new IllegalArgumentException("String 'id' cannot be blank");
    }
  }

}
