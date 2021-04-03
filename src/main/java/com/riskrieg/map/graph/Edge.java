package com.riskrieg.map.graph;

import java.util.Objects;

public class Edge { // TODO: Convert to record after Gson/Moshi add record support

  private final Territory source;
  private final Territory target;

  public Edge(Territory source, Territory target) {
    Objects.requireNonNull(source);
    Objects.requireNonNull(target);
    this.source = source;
    this.target = target;
  }

  public Territory source() {
    return source;
  }

  public Territory target() {
    return target;
  }

}
