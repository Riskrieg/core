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

  private RkmMap map;
  private MapOptions options;

  public GameMap() {
    this.map = null;
    this.options = null;
  }

  public boolean isSet() {
    return this.map != null && this.options != null;
  }

  public void set(RkmMap map, MapOptions options) {
    Objects.requireNonNull(map);
    Objects.requireNonNull(options);
    this.map = map;
    this.options = options;
  }

  public MapName getMapName() {
    return map.mapName();
  }

  public MapAuthor getAuthor() {
    return map.author();
  }

  public Graph<Territory, Border> getGraph() {
    var graph = new SimpleGraph<Territory, Border>(Border.class);
    for (Territory territory : map.getGraph().vertices()) {
      graph.addVertex(territory);
    }
    for (Border border : map.getGraph().edges()) {
      var source = map.getGraph().vertices().stream().filter(t -> t.id().equals(border.source())).findAny().orElse(null);
      var target = map.getGraph().vertices().stream().filter(t -> t.id().equals(border.target())).findAny().orElse(null);
      if (source != null && target != null) {
        graph.addEdge(source, target, border);
      }
    }
    return graph;
  }

  public MapImage getMapImage() {
    return map.getMapImage();
  }

  public MapOptions getOptions() {
    return options;
  }

  public Set<TerritoryId> getNeighbors(TerritoryId id) {
    var graph = getGraph();
    Territory territory = graph.vertexSet().stream().filter(t -> t.id().equals(id)).findAny().orElse(null);
    if (territory != null) {
      return Graphs.neighborSetOf(graph, territory).stream().map(Territory::id).collect(Collectors.toSet());
    }
    return new HashSet<>();
  }

  public boolean areNeighbors(TerritoryId source, TerritoryId target) {
    return getGraph().edgeSet().contains(new Border(source, target));
  }

  public boolean contains(TerritoryId id) {
    if (!isSet()) {
      return false;
    }
    return getGraph().vertexSet().stream().anyMatch(territory -> territory.id().equals(id));
  }

  public Territory get(TerritoryId id) {
    for (Territory t : map.getGraph().vertices()) {
      if (t.id().equals(id)) {
        return t;
      }
    }
    return null;
  }

}
