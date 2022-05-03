package com.riskrieg.core.internal.legacy;

import java.util.Objects;
import java.util.UUID;

public record LegacyGameId(String value) {

  public LegacyGameId {
    Objects.requireNonNull(value);
    if (value.isBlank()) {
      throw new IllegalArgumentException("String 'value' cannot be blank");
    }
  }

  private LegacyGameId() {
    this(UUID.randomUUID().toString());
  }

  public static LegacyGameId of(String id) {
    return new LegacyGameId(id);
  }

  public static LegacyGameId random() {
    return new LegacyGameId();
  }

}
