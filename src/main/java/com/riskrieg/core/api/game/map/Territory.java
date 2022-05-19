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

package com.riskrieg.core.api.game.map;

import com.riskrieg.core.api.game.map.territory.Nucleus;
import com.riskrieg.core.api.identifier.TerritoryIdentifier;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public record Territory(TerritoryIdentifier identifier, Set<Nucleus> nuclei) {

  public Territory {
    Objects.requireNonNull(identifier);
    Objects.requireNonNull(nuclei);
    if (nuclei.isEmpty()) {
      throw new IllegalStateException("Set<Nucleus> 'nuclei' must not be empty");
    }
  }

  public Territory(TerritoryIdentifier identifier, Nucleus nucleus) {
    this(identifier, Collections.singleton(nucleus));
  }

  public String name() {
    return identifier.id();
  }

}
