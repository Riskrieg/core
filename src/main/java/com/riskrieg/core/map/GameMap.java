package com.riskrieg.core.map;

import com.riskrieg.map.RkmMap;
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

  public RkmMap getMap() {
    return map;
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
