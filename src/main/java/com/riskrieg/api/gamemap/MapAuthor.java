package com.riskrieg.api.gamemap;

import java.util.Objects;

public final class MapAuthor {

  private final String name;

  public MapAuthor(String name) {
    Objects.requireNonNull(name);
    if (name.isBlank()) {
      throw new IllegalArgumentException("Field 'name' of type String cannot be blank");
    }
    this.name = name;
  }

  public String name() {
    return name;
  }

}
