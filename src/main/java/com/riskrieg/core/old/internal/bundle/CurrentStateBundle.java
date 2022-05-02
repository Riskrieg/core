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

package com.riskrieg.core.old.internal.bundle;

import com.riskrieg.core.old.api.gamemode.GameState;
import com.riskrieg.core.old.api.player.Player;
import edu.umd.cs.findbugs.annotations.Nullable;

public class CurrentStateBundle {

  private final Player currentTurnPlayer;
  private final GameState gameState;
  private final int claims;

  public CurrentStateBundle(Player currentTurnPlayer, GameState gameState, int claims) {
    this.currentTurnPlayer = currentTurnPlayer;
    this.gameState = gameState;
    this.claims = claims;
  }

  @Nullable
  public Player currentTurnPlayer() {
    return currentTurnPlayer;
  }

  public GameState gameState() {
    return gameState;
  }

  public int claims() {
    return claims;
  }

}
