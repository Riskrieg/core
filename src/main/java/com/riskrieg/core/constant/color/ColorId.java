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
import java.util.Objects;

public class ColorId implements Comparable<ColorId> {

  private final int value;

  private ColorId(int value) {
    if (value < 0) {
      throw new IllegalStateException("id must be greater or equal to than 0");
    }
    if (value > (Constants.MAX_PLAYERS - 1)) {
      throw new IllegalStateException("id must be less or equal to " + (Constants.MAX_PLAYERS - 1));
    }
    this.value = value;
  }

  public static ColorId of(int value) {
    return new ColorId(value);
  }

  public int value() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ColorId colorId = (ColorId) o;
    return value == colorId.value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public int compareTo(ColorId o) {
    return Integer.compare(this.value, o.value);
  }

}
