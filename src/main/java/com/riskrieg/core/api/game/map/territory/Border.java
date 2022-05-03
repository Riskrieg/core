package com.riskrieg.core.api.game.map.territory;

import java.util.Objects;

public record Border(String sourceId, String targetId) {

  public Border {
    Objects.requireNonNull(sourceId);
    Objects.requireNonNull(targetId);
    if (sourceId.equals(targetId)) {
      throw new IllegalStateException("sourceId and targetId cannot be equal");
    }
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
    return (sourceId.equals(border.sourceId) && targetId.equals(border.targetId)) || (sourceId.equals(border.targetId) && targetId.equals(border.sourceId));
  }

  @Override
  public int hashCode() {
    int hash = 17;
    int hashMultiplier = 79;
    int hashSum = sourceId.hashCode() + targetId.hashCode();
    hash = hashMultiplier * hash * hashSum;
    return hash;
  }

}
