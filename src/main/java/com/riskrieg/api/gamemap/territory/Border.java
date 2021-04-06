package com.riskrieg.api.gamemap.territory;

import java.util.Objects;

public final class Border {

  private final Territory source;
  private final Territory target;

  public Border(Territory source, Territory target) {
    Objects.requireNonNull(source);
    Objects.requireNonNull(target);
    this.source = source;
    this.target = target;
  }

  public Territory source() {
    return source;
  }

  public Territory target() {
    return target;
  }

}
