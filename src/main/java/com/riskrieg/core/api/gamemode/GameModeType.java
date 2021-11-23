/*
 *     Riskrieg, an open-source conflict simulation game.
 *     Copyright (C) 2021  Aaron Yoder <aaronjyoder@gmail.com>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
