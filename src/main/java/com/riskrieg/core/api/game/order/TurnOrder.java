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

package com.riskrieg.core.api.game.order;

import com.riskrieg.core.api.game.entity.nation.Nation;
import com.riskrieg.core.api.game.entity.player.Player;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.Collection;
import java.util.Deque;

@FunctionalInterface
public interface TurnOrder {

  /**
   * Takes in a collection of players and an associated collection of nations and returns a sorted deque of players.
   * <p>
   * The inputs are not modified in any way, and are assumed to be immutable collections.
   *
   * @param players The collection of players to sort.
   * @param nations The associated collection of nations used to help sort by.
   * @return the deque of players sorted according to the implementation.
   */
  @NonNull
  Deque<Player> getSorted(@NonNull final Collection<Player> players, @NonNull final Collection<Nation> nations);

}
