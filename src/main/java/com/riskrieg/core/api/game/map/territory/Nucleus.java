package com.riskrieg.core.api.game.map.territory;

import java.awt.Point;

public record Nucleus(int x, int y) {

  public Nucleus() {
    this(0, 0);
  }

  public Point toPoint() {
    return new Point(x, y);
  }

}
