package com.riskrieg.core.unsorted.gamemode;

public enum GameModeType {

  UNKNOWN("Unknown", "N/A"),
  CLASSIC("Classic", "The original game mode. No capitals, no alliances."),
  CONQUEST("Conquest", "The default game mode. Has capitals and alliances."),
  SIEGE("Siege", "Conquer enemy capitals to defeat them. Has capitals, has alliances."),
  BRAWL("Brawl", "Conquer enemy capitals to defeat them. Has capitals, no alliances."),
  CREATIVE("Creative", "A D&D-style game mode.");

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
