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
