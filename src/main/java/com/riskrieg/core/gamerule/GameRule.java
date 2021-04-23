package com.riskrieg.core.gamerule;

public enum GameRule {

  ALLIANCES("Alliances");

  private final String displayName;

  GameRule(String displayName) {
    this.displayName = displayName;
  }

  public String displayName() {
    return displayName;
  }

}
