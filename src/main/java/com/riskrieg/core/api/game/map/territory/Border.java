package com.riskrieg.core.api.game.map.territory;

import com.riskrieg.core.api.identifier.TerritoryIdentifier;
import java.util.Objects;

public record Border(TerritoryIdentifier source, TerritoryIdentifier target) {

  public Border {
    Objects.requireNonNull(source);
    Objects.requireNonNull(target);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Border border = (Border) o;
    return (source.equals(border.source) && target.equals(border.target)) || (source.equals(border.target) && target.equals(border.source));
  }

  @Override
  public int hashCode() {
    int hash = 17;
    int hashMultiplier = 79;
    int hashSum = source.hashCode() + target.hashCode();
    hash = hashMultiplier * hash * hashSum;
    return hash;
  }

}
