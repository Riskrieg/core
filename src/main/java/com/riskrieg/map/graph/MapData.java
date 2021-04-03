package com.riskrieg.map.graph;

import java.util.Objects;
import java.util.Set;
import org.jgrapht.Graph;

public class MapData { // TODO: Convert to record after Gson/Moshi add record support

  private final Set<Territory> vertices;
  private final Set<Edge> edges;

  public MapData(Set<Territory> vertices, Set<Edge> edges) {
    Objects.requireNonNull(vertices);
    Objects.requireNonNull(edges);
    this.vertices = vertices;
    this.edges = edges;
  }

  public MapData(Graph<Territory, Edge> graph) {
    this(Objects.requireNonNull(graph).vertexSet(), Objects.requireNonNull(graph).edgeSet());
  }

  public Set<Territory> vertices() {
    return vertices;
  }

  public Set<Edge> edges() {
    return edges;
  }

}
