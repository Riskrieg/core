package com.riskrieg.core.unsorted.map;

import com.riskrieg.map.RkmMap;
import com.riskrieg.map.data.MapAuthor;
import com.riskrieg.map.data.MapImage;
import com.riskrieg.map.data.MapName;
import com.riskrieg.map.edge.Border;
import com.riskrieg.map.territory.TerritoryId;
import com.riskrieg.map.vertex.Territory;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.SimpleGraph;

public final class GameMap {

  private RkmMap rkmMap;
  private MapOptions options;

  public GameMap() {
    this.rkmMap = null;
    this.options = null;
  }

  public GameMap(RkmMap rkmMap, MapOptions options) {
    Objects.requireNonNull(rkmMap);
    Objects.requireNonNull(options);
    this.rkmMap = rkmMap;
    this.options = options;
  }

  public boolean isSet() {
    return this.rkmMap != null && this.options != null;
  }

  public void set(RkmMap rkmMap, MapOptions options) {
    Objects.requireNonNull(rkmMap);
    Objects.requireNonNull(options);
    this.rkmMap = rkmMap;
    this.options = options;
  }

  public MapName mapName() {
    return rkmMap.mapName();
  }

  public MapAuthor author() {
    return rkmMap.author();
  }

  public MapImage mapImage() {
    return rkmMap.mapImage();
  }

  public MapOptions options() {
    return options;
  }

  public Graph<Territory, Border> graph() {
    var graph = new SimpleGraph<Territory, Border>(Border.class);
    for (Territory territory : rkmMap.graph().vertices()) {
      graph.addVertex(territory);
    }
    for (Border border : rkmMap.graph().edges()) {
      var source = rkmMap.graph().vertices().stream().filter(t -> t.id().equals(border.source())).findAny().orElse(null);
      var target = rkmMap.graph().vertices().stream().filter(t -> t.id().equals(border.target())).findAny().orElse(null);
      if (source != null && target != null) {
        graph.addEdge(source, target, border);
      }
    }
    return graph;
  }

  public Set<TerritoryId> getNeighbors(TerritoryId id) {
    var graph = graph();
    Territory territory = graph.vertexSet().stream().filter(t -> t.id().equals(id)).findAny().orElse(null);
    if (territory != null) {
      return Graphs.neighborSetOf(graph, territory).stream().map(Territory::id).collect(Collectors.toSet());
    }
    return new HashSet<>();
  }

  public boolean areNeighbors(TerritoryId source, TerritoryId target) {
    return graph().edgeSet().contains(new Border(source, target));
  }

  public boolean contains(TerritoryId id) {
    if (!isSet()) {
      return false;
    }
    return graph().vertexSet().stream().anyMatch(territory -> territory.id().equals(id));
  }

  public Territory get(TerritoryId id) {
    for (Territory t : rkmMap.graph().vertices()) {
      if (t.id().equals(id)) {
        return t;
      }
    }
    return null;
  }

}
