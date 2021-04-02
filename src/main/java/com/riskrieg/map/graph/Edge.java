package com.riskrieg.map.graph;

import java.util.Objects;

public record Edge(Territory source, Territory target) {

  public Edge {
    Objects.requireNonNull(source);
    Objects.requireNonNull(target);
  }

}
