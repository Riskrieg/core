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

package com.riskrieg.core.api.game.map.territory;

import java.util.Objects;

public record Border(String sourceId, String targetId) {

  public Border {
    Objects.requireNonNull(sourceId);
    Objects.requireNonNull(targetId);
    if (sourceId.equals(targetId)) {
      throw new IllegalStateException("sourceId and targetId cannot be equal");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Border border = (Border) o;
    return (sourceId.equals(border.sourceId) && targetId.equals(border.targetId)) || (sourceId.equals(border.targetId) && targetId.equals(border.sourceId));
  }

  @Override
  public int hashCode() {
    int hash = 17;
    int hashMultiplier = 79;
    int hashSum = sourceId.hashCode() + targetId.hashCode();
    hash = hashMultiplier * hash * hashSum;
    return hash;
  }

}
