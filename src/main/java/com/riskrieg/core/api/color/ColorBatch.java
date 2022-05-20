package com.riskrieg.core.api.color;

import com.riskrieg.core.api.game.Game;
import java.awt.Color;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public record ColorBatch(SortedSet<GameColor> set) {

  public ColorBatch {
    if (set.size() != Game.MAX_PLAYERS) {
      throw new IllegalStateException("Your color set must have " + Game.MAX_PLAYERS + " player colors defined. You have " + set.size() + " unique items in your set.");
    }
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

}
