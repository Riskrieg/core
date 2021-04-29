package com.riskrieg.core.unsorted.gamerule;

public enum GameRule {

  LONG_SETUP("Long Setup"),
  CAP_ALLIES("Cap Per-Player Alliances");

  private final String displayName;

  GameRule(String displayName) {
    this.displayName = displayName;
  }

  public String displayName() {
    return displayName;
  }

}
