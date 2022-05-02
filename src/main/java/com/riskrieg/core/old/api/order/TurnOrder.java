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
import java.util.Collection;
import java.util.Deque;

public interface TurnOrder {

  @NonNull
  String displayName();

  @NonNull
  String description();

  /**
   * This method should always return a Deque that has the same elements, and number of elements, as the input Collection, regardless of implementation.
   *
   * @param players The collection of players to sort.
   * @return A Deque of players sorted based on the implementation.
   */
  @NonNull
  Deque<Player> sort(@NonNull final Collection<Player> players);

}
