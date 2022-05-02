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

package com.riskrieg.core.old.api.map.options;

import com.riskrieg.core.old.api.map.options.alignment.HorizontalAlignment;
import com.riskrieg.core.old.api.map.options.alignment.VerticalAlignment;
import java.util.Objects;

public final class InterfaceAlignment {

  private final VerticalAlignment vertical;
  private final HorizontalAlignment horizontal;

  public InterfaceAlignment(VerticalAlignment vertical, HorizontalAlignment horizontal) {
    this.vertical = Objects.requireNonNull(vertical, "Vertical alignment value cannot be null");
    this.horizontal = Objects.requireNonNull(horizontal, "Horizontal alignment value cannot be null");
  }

  public VerticalAlignment vertical() {
    return vertical;
  }

  public HorizontalAlignment horizontal() {
    return horizontal;
  }

}
