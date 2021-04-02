package com.riskrieg.player;

import java.util.Objects;
import java.util.UUID;

public record PlayerIdentifier(String id, PlayerColor color) {

  public PlayerIdentifier {
    Objects.requireNonNull(id);
    Objects.requireNonNull(color);
    if (id.isBlank()) {
      throw new IllegalArgumentException("id cannot be blank");
    }
  }

  public PlayerIdentifier(PlayerColor color) {
    this(UUID.randomUUID().toString(), color);
  }

}
