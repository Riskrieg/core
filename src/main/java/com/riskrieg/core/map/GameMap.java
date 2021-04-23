package com.riskrieg.core.map;

import com.riskrieg.map.RkmMap;
import com.riskrieg.map.data.MapAuthor;
import com.riskrieg.map.data.MapGraph;
import com.riskrieg.map.data.MapImage;
import com.riskrieg.map.data.MapName;
import java.util.Objects;

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

  public MapGraph getGraph() {
    return map.getGraph();
  }

  public MapImage getMapImage() {
    return map.getMapImage();
  }

  public MapOptions getOptions() {
    return options;
  }

  public void set(RkmMap map, MapOptions options) {
    Objects.requireNonNull(map);
    Objects.requireNonNull(options);
    this.map = map;
    this.options = options;
  }

}
