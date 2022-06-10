package com.riskrieg.core.api.game.feature;

import java.util.Objects;

public record FeatureFlag(Feature feature, boolean enabled) {

  public FeatureFlag {
    Objects.requireNonNull(feature);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FeatureFlag that = (FeatureFlag) o;
    return feature == that.feature;
  }

  @Override
  public int hashCode() {
    return Objects.hash(feature);
  }

}
