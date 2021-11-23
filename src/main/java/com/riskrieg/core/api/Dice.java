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

package com.riskrieg.core.api;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public final class Dice {

  private final int sides;
  private final int amount;

  public Dice(int sides, int amount) {
    this.sides = sides;
    this.amount = amount;
  }

  public int[] roll() {
    return IntStream.generate(this::rollOne).limit(amount).toArray();
  }

  private int rollOne() {
    return ThreadLocalRandom.current().nextInt(sides) + 1;
  }

  public int sides() {
    return sides;
  }

  public int amount() {
    return amount;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    var that = (Dice) obj;
    return this.sides == that.sides && this.amount == that.amount;
  }

  @Override
  public int hashCode() {
    return Objects.hash(sides, amount);
  }

  @Override
  public String toString() {
    return "Dice[" + "sides=" + sides + ", " + "amount=" + amount + ']';
  }


}
