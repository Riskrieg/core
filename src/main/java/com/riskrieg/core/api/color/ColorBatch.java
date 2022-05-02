package com.riskrieg.core.api.color;

import com.riskrieg.core.api.game.Game;
import java.awt.Color;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public record ColorBatch(SortedSet<GameColor> colorSet) {

  public ColorBatch {
    if (colorSet.size() != Game.MAX_PLAYERS) {
      throw new IllegalStateException("Your color set must have " + Game.MAX_PLAYERS + " player colors defined. You have " + colorSet.size() + " unique items in your set.");
    }
  }

  public ColorBatch(GameColor... colors) {
    this(new TreeSet<>(Set.of(colors)));
  }

  public GameColor first() {
    return colorSet.first();
  }

  public GameColor last() {
    return colorSet.last();
  }

  public GameColor valueOf(Color color) {
    for (GameColor gameColor : colorSet) {
      if (gameColor.toColor().equals(color)) {
        return gameColor;
      }
    }
    return last();
  }

}
