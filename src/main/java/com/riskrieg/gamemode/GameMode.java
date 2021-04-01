package com.riskrieg.gamemode;

public enum GameMode {

  CONQUEST("Conquest", "Claim land and attack foes with the ultimate goal to rule over everything."),
  CREATIVE("Creative", "Allows you to play maps in a sandbox D&D style where one player, the Dungeon Master, controls everything.");

  private final String name;
  private final String description;

  GameMode(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

}
