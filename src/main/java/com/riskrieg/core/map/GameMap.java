package com.riskrieg.core.map;

import com.riskrieg.map.RkmMap;
import com.riskrieg.map.data.MapAuthor;
import com.riskrieg.map.data.MapImage;
import com.riskrieg.map.data.MapName;
import com.riskrieg.map.edge.Border;
import com.riskrieg.map.territory.TerritoryId;
import com.riskrieg.map.vertex.Territory;
import java.util.Objects;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleGraph;

public class GameMap {

  private RkmMap map;
  private MapOptions options;

  public GameMap() {
    this.map = null;
    this.options = null;
  }

  public boolean isSet() {
    return this.map != null && this.options != null;
  }

  public MapName mapName() {
    return map.mapName();
  }

  public MapAuthor author() {
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

  public boolean neighbors(TerritoryId source, TerritoryId target) {
    return getGraph().edgeSet().contains(new Border(source, target));
  }

  public boolean contains(TerritoryId id) {
    if (!isSet()) {
      return false;
    }
    return getGraph().vertexSet().stream().anyMatch(territory -> territory.id().equals(id));
  }

  public void set(RkmMap map, MapOptions options) {
    Objects.requireNonNull(map);
    Objects.requireNonNull(options);
    this.map = map;
    this.options = options;
  }

}
