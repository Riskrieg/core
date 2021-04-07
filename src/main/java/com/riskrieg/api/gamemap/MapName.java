package com.riskrieg.api.gamemap;

public class MapName {

  private final String name;
  private final String displayName;

  public MapName(final String name, final String displayName) {
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
