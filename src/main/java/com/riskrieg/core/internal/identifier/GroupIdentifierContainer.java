package com.riskrieg.core.internal.identifier;

import com.riskrieg.core.api.identifier.GroupIdentifier;
import java.util.Objects;

public record GroupIdentifierContainer(String id) implements GroupIdentifier {

  public GroupIdentifierContainer {
    Objects.requireNonNull(id);
    if (id.isBlank()) {
      throw new IllegalArgumentException("String 'id' cannot be blank");
    }
  }

}
