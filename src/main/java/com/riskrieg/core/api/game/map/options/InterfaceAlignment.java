package com.riskrieg.core.api.game.map.options;

import java.util.Objects;

public record InterfaceAlignment(VerticalAlignment vertical, HorizontalAlignment horizontal) {

  public InterfaceAlignment {
    Objects.requireNonNull(vertical);
    Objects.requireNonNull(horizontal);
  }

}
