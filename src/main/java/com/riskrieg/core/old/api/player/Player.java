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

package com.riskrieg.core.old.api.player;

import com.riskrieg.core.old.constant.color.ColorId;
import java.util.Objects;

public final class Player {

  private ColorId colorId;
  private final Identity identity;
  private String name;

  public Player(Identity identity, ColorId colorId, String name) {
    Objects.requireNonNull(identity);
    Objects.requireNonNull(colorId);
    Objects.requireNonNull(name);
    if (name.isBlank()) {
      throw new IllegalArgumentException("name cannot be blank");
    }
    this.identity = identity;
    this.colorId = colorId;
    this.name = name;
  }

  public Player(ColorId colorId, String name) {
    this(Identity.random(), colorId, name);
  }

  public Identity identity() {
    return identity;
  }

  public ColorId colorId() {
    return colorId;
  }

  public String name() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Player player = (Player) o;
    return identity.equals(player.identity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(identity);
  }

}
