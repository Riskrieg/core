package com.riskrieg.core.constant.color;

import com.riskrieg.core.constant.Constants;
import java.util.Objects;

public class ColorId implements Comparable<ColorId> {

  private final int value;

  private ColorId(int value) {
    if (value < 0) {
      throw new IllegalStateException("id must be greater or equal to than 0");
    }
    if (value > (Constants.MAX_PLAYERS - 1)) {
      throw new IllegalStateException("id must be less or equal to " + (Constants.MAX_PLAYERS - 1));
    }
    this.value = value;
  }

  public static ColorId of(int value) {
    return new ColorId(value);
  }

  public int value() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ColorId colorId = (ColorId) o;
    return value == colorId.value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public int compareTo(ColorId o) {
    return Integer.compare(this.value, o.value);
  }

}
