package com.riskrieg.core.constant.color;

import com.riskrieg.core.constant.Constants;
import java.awt.Color;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class ColorBatch {

  private final SortedSet<PlayerColor> colorSet;

  public ColorBatch(SortedSet<PlayerColor> colorSet) {
    if (colorSet.size() != Constants.MAX_PLAYERS) {
      throw new IllegalStateException("Your color set must have " + Constants.MAX_PLAYERS + " player colors defined. You have " + colorSet.size() + " unique items in your set.");
    }
    this.colorSet = Collections.unmodifiableSortedSet(colorSet);
  }

  public ColorBatch(PlayerColor... colors) {
    this(new TreeSet<>(Set.of(colors)));
  }

  public SortedSet<PlayerColor> toSet() {
    return colorSet;
  }

  public PlayerColor first() {
    return colorSet.first();
  }

  public PlayerColor last() {
    return colorSet.last();
  }

  public PlayerColor valueOf(Color color) {
    for (PlayerColor pc : colorSet) {
      if (pc.value().equals(color)) {
        return pc;
      }
    }
    return last();
  }

}
