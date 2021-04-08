package com.riskrieg.api.gamemap.territory;

import java.util.Objects;
import java.util.Set;
import org.jgrapht.Graph;

public final class MapGraph {

  private final Set<Territory> vertices;
  private final Set<Border> edges;

  public MapGraph(Set<Territory> vertices, Set<Border> edges) {
    Objects.requireNonNull(vertices);
    Objects.requireNonNull(edges);
    if (vertices.isEmpty()) {
      throw new IllegalStateException("Field 'vertices' of type Set<Territory> must not be empty");
    }
    if (edges.isEmpty()) {
      throw new IllegalStateException("Field 'edges' of type Set<Border> must not be empty");
    }
    this.vertices = vertices;
    this.edges = edges;
  }

  public MapGraph(Graph<Territory, Border> graph) {
    this(Objects.requireNonNull(graph).vertexSet(), Objects.requireNonNull(graph).edgeSet());
  }

  public Set<Territory> vertices() {
    return vertices;
  }

  public Set<Border> edges() {
    return edges;
  }

}
