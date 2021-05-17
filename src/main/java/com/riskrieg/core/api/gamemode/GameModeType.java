package com.riskrieg.core.api.gamemode;

public enum GameModeType {

  UNKNOWN("Unknown", "N/A"),
  CLASSIC("Classic", "The original, simpler game mode. No capitals, no alliances."),
  CONQUEST("Conquest", "The default game mode. Has capitals and alliances."),
  REGICIDE("Regicide", "Conquer enemy capitals to defeat them. Has capitals, has alliances."),
  BRAWL("Brawl", "Build up your territories until no more are left, then attack. Has capitals, no alliances."),
  CREATIVE("Creative", "A D&D-style role-playing game mode.");

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
