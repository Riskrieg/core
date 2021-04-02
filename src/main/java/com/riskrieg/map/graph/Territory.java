package com.riskrieg.map.graph;

import java.awt.Point;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public record Territory(String name, Set<Point> seedPoints) implements Comparable<Territory> {

  public Territory {
    Objects.requireNonNull(name);
    Objects.requireNonNull(seedPoints);
    if (seedPoints.isEmpty()) {
      throw new IllegalStateException("seedPoints must not be empty");
    }
  }

  public Territory(String name, Point seedPoint) {
    this(name, Collections.singleton(seedPoint));
  }

  @Override
  public int compareTo(Territory o) {
    return this.name().compareTo(o.name());
  }

}
