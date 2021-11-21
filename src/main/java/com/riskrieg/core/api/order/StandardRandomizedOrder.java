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

package com.riskrieg.core.api.order;

import com.riskrieg.core.api.player.Player;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import javax.annotation.Nonnull;

public class StandardRandomizedOrder implements TurnOrder {

  @Nonnull
  @Override
  public String displayName() {
    return "Standard (Randomized Start)";
  }

  @Nonnull
  @Override
  public String description() {
    return "Players take turns according to their color, from top to bottom, and the player who goes first is randomized.";
  }

  @Nonnull
  @Override
  public Deque<Player> sort(@Nonnull final Collection<Player> players) {
    List<Player> playerList = new ArrayList<>(players);
    playerList.sort(new StandardComparator());
    int start = new Random().nextInt(playerList.size());
    Deque<Player> playerDeque = new ArrayDeque<>(playerList);
    for (int i = 0; i < start; i++) {
      playerDeque.addLast(playerDeque.removeFirst());
    }
    return new ArrayDeque<>(playerDeque);
  }

}
