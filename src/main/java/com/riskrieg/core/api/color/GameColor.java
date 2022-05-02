package com.riskrieg.core.api.color;

import edu.umd.cs.findbugs.annotations.NonNull;
import java.awt.Color;

public record GameColor(int id, String name, int r, int g, int b) implements Comparable<GameColor> {

  public GameColor {
    if (name.isBlank()) {
      throw new IllegalStateException("GameColor name cannot be blank");
    }
    if (r < 0 || r > 255) {
      throw new IllegalStateException("GameColor value for red must be between 0 and 255, inclusive on either end.");
    }
    if (g < 0 || g > 255) {
      throw new IllegalStateException("GameColor value for green must be between 0 and 255, inclusive on either end.");
    }
    if (b < 0 || b > 255) {
      throw new IllegalStateException("GameColor value for blue must be between 0 and 255, inclusive on either end.");
    }
  }

  public int id() {
    return id;
  }

  public Color toColor() {
    return new Color(r, g, b);
  }

  @Override
  public int compareTo(@NonNull GameColor o) {
    return Integer.compare(this.id(), o.id());
  }

}
