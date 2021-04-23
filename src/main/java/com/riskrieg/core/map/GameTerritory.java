package com.riskrieg.core.map;

import com.riskrieg.map.territory.TerritoryId;
import java.util.Objects;

public final class GameTerritory {

  private final TerritoryId id;
  private final TerritoryType type;

  public GameTerritory(TerritoryId id, TerritoryType type) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(type);
    this.id = id;
    this.type = type;
  }

  public GameTerritory(TerritoryId id) {
    this(id, TerritoryType.NORMAL);
  }

  public TerritoryId id() {
    return id;
  }

  public TerritoryType type() {
    return type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameTerritory that = (GameTerritory) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

}
