package com.riskrieg.core.gamerule;

public enum GameRule {

  CAP_ALLIANCES("Cap Per-Player Alliances");

  private final String displayName;

  GameRule(String displayName) {
    this.displayName = displayName;
  }

  public String displayName() {
    return displayName;
  }

}
