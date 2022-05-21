package com.riskrieg.core.api.identifier;

import java.util.Objects;
import java.util.UUID;

public record NationIdentifier(String id) implements Identifier {

  public NationIdentifier {
    Objects.requireNonNull(id);
    if (id.isBlank()) {
      throw new IllegalStateException("String 'id' cannot be blank");
    }
  }

  public static NationIdentifier of(String id) {
    return new NationIdentifier(id);
  }

  public static NationIdentifier uuid() {
    return new NationIdentifier(UUID.randomUUID().toString());
  }

}
