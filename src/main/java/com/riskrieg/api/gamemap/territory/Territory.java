package com.riskrieg.api.gamemap.territory;

import java.awt.Point;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public final class Territory {

  private final String name;
  private final Set<Point> seedPoints;

  public Territory(String name, Set<Point> seedPoints) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(seedPoints);
    if (name.isBlank()) {
      throw new IllegalArgumentException("String 'name' cannot be blank");
    }
    if (seedPoints.isEmpty()) {
      throw new IllegalStateException("Set<Point> 'seedPoints' must not be empty");
    }
    this.name = name;
    this.seedPoints = seedPoints;
  }

  public Territory(String name, Point seedPoint) {
    this(name, Collections.singleton(seedPoint));
  }

  public String name() {
    return name;
  }

  public Set<Point> seedPoints() {
    return seedPoints;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Territory territory = (Territory) o;
    return name.equals(territory.name) && seedPoints.equals(territory.seedPoints);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, seedPoints);
  }

}
