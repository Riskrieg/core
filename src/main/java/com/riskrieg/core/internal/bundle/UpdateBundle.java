/**
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

package com.riskrieg.core.internal.bundle;

import com.riskrieg.core.api.gamemode.GameState;
import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.internal.GameEndReason;
import java.util.Set;
import javax.annotation.Nullable;

public final class UpdateBundle {

  private final Player currentTurnPlayer;
  private final Player previousPlayer;
  private final GameState gameState;
  private final GameEndReason reason;
  private final int claims;
  private final Set<Player> defeated;

  public UpdateBundle(Player currentTurnPlayer, Player previousPlayer, GameState gameState, GameEndReason reason, int claims, Set<Player> defeated) {
    this.currentTurnPlayer = currentTurnPlayer;
    this.previousPlayer = previousPlayer;
    this.gameState = gameState;
    this.reason = reason;
    this.claims = claims;
    this.defeated = defeated;
  }

  @Nullable
  public Player previousPlayer() {
    return previousPlayer;
  }

  @Nullable
  public Player currentTurnPlayer() {
    return currentTurnPlayer;
  }

  public GameState gameState() {
    return gameState;
  }

  public GameEndReason reason() {
    return reason;
  }

  public int claims() {
    return claims;
  }

  public Set<Player> defeated() {
    return defeated;
  }

}
