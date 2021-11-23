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

public final class AllianceBundle {

  private final Player player1;
  private final Player player2;
  private final GameEndReason reason;

  public AllianceBundle(Player player1, Player player2, GameEndReason reason) {
    this.player1 = player1;
    this.player2 = player2;
    this.reason = reason;
  }

  public Player player1() {
    return player1;
  }

  public Player player2() {
    return player2;
  }

  public GameEndReason reason() {
    return reason;
  }

}
