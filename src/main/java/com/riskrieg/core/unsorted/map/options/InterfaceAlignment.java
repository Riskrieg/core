package com.riskrieg.core.unsorted.map.options;

import com.riskrieg.core.unsorted.map.options.alignment.HorizontalAlignment;
import com.riskrieg.core.unsorted.map.options.alignment.VerticalAlignment;
import java.util.Objects;

public final class InterfaceAlignment {

  private final VerticalAlignment vertical;
  private final HorizontalAlignment horizontal;

  public InterfaceAlignment(VerticalAlignment vertical, HorizontalAlignment horizontal) {
    this.vertical = Objects.requireNonNull(vertical, "Vertical alignment value cannot be null");
    this.horizontal = Objects.requireNonNull(horizontal, "Horizontal alignment value cannot be null");
  }

  public VerticalAlignment vertical() {
    return vertical;
  }

  public HorizontalAlignment horizontal() {
    return horizontal;
  }

}
