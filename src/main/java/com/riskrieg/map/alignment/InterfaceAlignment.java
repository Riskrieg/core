package com.riskrieg.map.alignment;

public class InterfaceAlignment { // TODO: Convert to record after Gson/Moshi add record support

  private final VerticalAlignment vertical;
  private final HorizontalAlignment horizontal;

  public InterfaceAlignment(VerticalAlignment vertical, HorizontalAlignment horizontal) {
    this.vertical = vertical;
    this.horizontal = horizontal;
  }

  public VerticalAlignment vertical() {
    return vertical;
  }

  public HorizontalAlignment horizontal() {
    return horizontal;
  }

}
