/*
 *     Riskrieg, an open-source conflict simulation game.
 *     Copyright (C) 2021 Aaron Yoder <aaronjyoder@gmail.com> and Contributors
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

package com.riskrieg.core.api.game.mode;

import com.riskrieg.core.api.game.GameMode;
import edu.umd.cs.findbugs.annotations.NonNull;

public enum StandardMode implements GameMode {

  CONQUEST("Conquest", "The default game mode.");

  private final String displayName;
  private final String description;

  StandardMode(String displayName, String description) {
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
