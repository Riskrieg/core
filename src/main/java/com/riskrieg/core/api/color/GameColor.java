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

package com.riskrieg.core.api.color;

import edu.umd.cs.findbugs.annotations.NonNull;
import java.awt.Color;
import java.util.Objects;

public record GameColor(int id, String name, int r, int g, int b) implements Comparable<GameColor> {

  public GameColor {
    if (name.isBlank()) {
      throw new IllegalStateException("String 'name' cannot be blank");
    }
    if (r < 0 || r > 255) {
      throw new IllegalStateException("GameColor value for red must be between 0 and 255, inclusive on either end.");
    }
    if (g < 0 || g > 255) {
      throw new IllegalStateException("GameColor value for green must be between 0 and 255, inclusive on either end.");
    }
    if (b < 0 || b > 255) {
      throw new IllegalStateException("GameColor value for blue must be between 0 and 255, inclusive on either end.");
    }
  }

  public int id() {
    return id;
  }

  public Color toColor() {
    return new Color(r, g, b);
  }

  @Override
  public int compareTo(@NonNull GameColor o) {
    return Integer.compare(this.id(), o.id());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameColor gameColor = (GameColor) o;
    return id == gameColor.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

}
