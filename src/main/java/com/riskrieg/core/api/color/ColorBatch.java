package com.riskrieg.core.api.color;

import java.awt.Color;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public record ColorBatch(SortedSet<GameColor> set) {

  public static final int MINIMUM_SIZE = 2;
  public static final int MAXIMUM_SIZE = 16;

  public ColorBatch {
    if (set.size() < MINIMUM_SIZE) {
      throw new IllegalStateException("Your color set cannot have fewer than " + MINIMUM_SIZE + " colors defined. You have " + set.size() + " unique items in your set.");
    } else if (set.size() > MAXIMUM_SIZE) {
      throw new IllegalStateException("Your color set cannot have more than " + MAXIMUM_SIZE + " colors defined. You have " + set.size() + " unique items in your set.");
    }
    set = Collections.unmodifiableSortedSet(set);
  }

  public ColorBatch(GameColor... colors) {
    this(new TreeSet<>(Set.of(colors)));
  }

  public GameColor first() {
    return set.first();
  }

  public GameColor last() {
    return set.last();
  }

  public GameColor valueOf(Color color) {
    for (GameColor gameColor : set) {
      if (gameColor.toColor().equals(color)) {
        return gameColor;
      }
    }
    return last();
  }

  public static ColorBatch standard() {
    return new ColorBatch(
        new GameColor(0, "Salmon", 255, 140, 150), new GameColor(1, "Lavender", 155, 120, 190),
        new GameColor(2, "Thistle", 215, 190, 240), new GameColor(3, "Ice", 195, 230, 255),
        new GameColor(4, "Sky", 120, 165, 215), new GameColor(5, "Sea", 140, 225, 175),
        new GameColor(6, "Forest", 85, 155, 60), new GameColor(7, "Sod", 170, 190, 95),
        new GameColor(8, "Cream", 255, 254, 208), new GameColor(9, "Sun", 240, 225, 80),
        new GameColor(10, "Gold", 255, 195, 5), new GameColor(11, "Cadmium", 250, 105, 65),
        new GameColor(12, "Sanguine", 95, 10, 0), new GameColor(13, "Mocha", 75, 40, 0),
        new GameColor(14, "Matte", 30, 30, 30), new GameColor(15, "Cobalt", 0, 50, 120)
    );
  }

}
