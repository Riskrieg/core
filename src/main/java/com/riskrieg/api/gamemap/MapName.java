package com.riskrieg.api.gamemap;

import java.util.Objects;

public final class MapName {

  private final String name;
  private final String displayName;

  public MapName(String name, String displayName) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(displayName);
    if (name.isBlank()) {
      throw new IllegalArgumentException("Field 'name' of type String cannot be blank");
    }
    if (displayName.isBlank()) {
      throw new IllegalArgumentException("Field 'displayName' of type String cannot be blank");
    }
    this.name = name;
    this.displayName = displayName;
  }

  public String name() {
    return name;
  }

  public String displayName() {
    return displayName;
  }

}
