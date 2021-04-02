package com.riskrieg.map.graph;

import java.util.Objects;
import java.util.Set;
import org.jgrapht.Graph;

public record MapData(Set<Territory> vertices, Set<Edge> edges) {

  public MapData {
    Objects.requireNonNull(vertices);
    Objects.requireNonNull(edges);
  }

  public MapData(Graph<Territory, Edge> graph) {
    this(Objects.requireNonNull(graph).vertexSet(), Objects.requireNonNull(graph).edgeSet());
  }

}
