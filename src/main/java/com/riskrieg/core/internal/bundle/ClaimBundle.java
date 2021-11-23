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

import com.riskrieg.map.territory.TerritoryId;
import java.util.Collections;
import java.util.Set;

public final class ClaimBundle {

  private final Set<TerritoryId> claimed;
  private final Set<TerritoryId> taken;
  private final Set<TerritoryId> defended;

  public ClaimBundle(Set<TerritoryId> claimed, Set<TerritoryId> taken, Set<TerritoryId> defended) {
    this.claimed = claimed;
    this.taken = taken;
    this.defended = defended;
  }

  public Set<TerritoryId> claimed() {
    return Collections.unmodifiableSet(claimed);
  }

  public Set<TerritoryId> taken() {
    return Collections.unmodifiableSet(taken);
  }

  public Set<TerritoryId> defended() {
    return Collections.unmodifiableSet(defended);
  }

}
