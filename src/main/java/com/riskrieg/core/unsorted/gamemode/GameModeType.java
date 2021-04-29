package com.riskrieg.core.unsorted.gamemode;

public enum GameModeType {

  UNKNOWN("Unknown", "N/A"),
  CONQUEST("Conquest", "The default game mode.");

  private final String displayName;
  private final String description;

  GameModeType(String displayName, String description) {
    this.displayName = displayName;
    this.description = description;
  }

  public String displayName() {
    return displayName;
  }

  public String description() {
    return description;
  }

}
