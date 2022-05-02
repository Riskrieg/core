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

package com.riskrieg.core.old.api.order;

import com.riskrieg.core.old.api.player.Player;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

public final class FullRandomOrder implements TurnOrder {

  @NonNull
  @Override
  public String displayName() {
    return "Full Random";
  }

  @NonNull
  @Override
  public String description() {
    return "Players take turns in an order that is randomized at the start of the game, and the player who goes first is randomized.";
  }

  @NonNull
  @Override
  public Deque<Player> sort(@NonNull final Collection<Player> players) {
    Objects.requireNonNull(players);
    List<Player> playerList = new ArrayList<>(players);
    Collections.shuffle(playerList);
    return new ArrayDeque<>(playerList);
  }

}
