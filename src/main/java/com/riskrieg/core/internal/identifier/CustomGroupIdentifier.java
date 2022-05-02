package com.riskrieg.core.internal.identifier;

import com.riskrieg.core.api.identifier.GroupIdentifier;
import java.util.Objects;

public record CustomGroupIdentifier(String id) implements GroupIdentifier {

  public CustomGroupIdentifier {
    Objects.requireNonNull(id);
    if (id.isBlank()) {
      throw new IllegalArgumentException("String 'id' cannot be blank");
    }
  }

}
