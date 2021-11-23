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

package com.riskrieg.core.constant.color;

import com.riskrieg.core.constant.Constants;
import java.awt.Color;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class ColorBatch {

  private final SortedSet<PlayerColor> colorSet;

  public ColorBatch(SortedSet<PlayerColor> colorSet) {
    if (colorSet.size() != Constants.MAX_PLAYERS) {
      throw new IllegalStateException("Your color set must have " + Constants.MAX_PLAYERS + " player colors defined. You have " + colorSet.size() + " unique items in your set.");
    }
    this.colorSet = Collections.unmodifiableSortedSet(colorSet);
  }

  public ColorBatch(PlayerColor... colors) {
    this(new TreeSet<>(Set.of(colors)));
  }

  public SortedSet<PlayerColor> toSet() {
    return colorSet;
  }

  public PlayerColor first() {
    return colorSet.first();
  }

  public PlayerColor last() {
    return colorSet.last();
  }

  public PlayerColor valueOf(Color color) {
    for (PlayerColor pc : colorSet) {
      if (pc.value().equals(color)) {
        return pc;
      }
    }
    return last();
  }

}
