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

package com.riskrieg.core.internal.bundle;

import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.internal.GameEndReason;
import javax.annotation.Nullable;

public final class LeaveBundle {

  private final Player currentTurnPlayer;
  private final Player leavingPlayer;
  private final GameEndReason reason;
  private final int claims;

  public LeaveBundle(Player currentTurnPlayer, Player leavingPlayer, GameEndReason reason, int claims) {
    this.currentTurnPlayer = currentTurnPlayer;
    this.leavingPlayer = leavingPlayer;
    this.reason = reason;
    this.claims = claims;
  }

  @Nullable
  public Player currentTurnPlayer() {
    return currentTurnPlayer;
  }

  public Player leavingPlayer() {
    return leavingPlayer;
  }

  public GameEndReason reason() {
    return reason;
  }

  public int claims() {
    return claims;
  }

}
