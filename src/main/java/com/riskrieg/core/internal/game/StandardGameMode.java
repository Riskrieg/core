package com.riskrieg.core.internal.game;

import com.riskrieg.core.api.game.GameMode;
import edu.umd.cs.findbugs.annotations.NonNull;

public enum StandardGameMode implements GameMode {

  UNKNOWN("Unknown", "N/A"),
  //  CLASSIC("Classic", "The original, simpler game mode. No capitals, no alliances."),
  CONQUEST("Conquest", "The default game mode. Has capitals, has alliances.");
//  REGICIDE("Regicide", "Conquer enemy capitals to defeat them. Has capitals, has alliances."),
//  BRAWL("Brawl", "Build up your territories until no more are left, then attack. Has capitals, no alliances.");

  private final String displayName;
  private final String description;

  StandardGameMode(String displayName, String description) {
    this.displayName = displayName;
    this.description = description;
  }

  @NonNull
  @Override
  public String displayName() {
    return displayName;
  }

  @NonNull
  @Override
  public String description() {
    return description;
  }

}
