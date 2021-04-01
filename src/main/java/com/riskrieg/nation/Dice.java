package com.riskrieg.nation;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class Dice { // TODO: Convert to Record when Java 16 comes out

  private final int sides;
  private final int amount;

  public Dice(int sides, int amount) {
    this.sides = sides;
    this.amount = amount;
  }

  public int sides() {
    return sides;
  }

  public int amount() {
    return amount;
  }

  public int[] roll() {
    return IntStream.generate(this::rollOne).limit(amount).toArray();
  }

  private int rollOne() {
    return ThreadLocalRandom.current().nextInt(sides) + 1;
  }

}
