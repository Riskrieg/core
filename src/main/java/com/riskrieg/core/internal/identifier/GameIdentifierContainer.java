package com.riskrieg.core.internal.identifier;

import com.riskrieg.core.api.identifier.GameIdentifier;
import java.util.Objects;

public record GameIdentifierContainer(String id) implements GameIdentifier {

  public GameIdentifierContainer {
    Objects.requireNonNull(id);
    if (id.isBlank()) {
      throw new IllegalArgumentException("String 'id' cannot be blank");
    }
  }

}
