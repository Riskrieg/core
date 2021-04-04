package com.riskrieg.player;

import java.awt.Color;

public enum PlayerColor {

  SALMON(255, 140, 150),
  LAVENDER(155, 120, 190),
  THISTLE(215, 190, 240),
  ICE(195, 230, 255),
  SKY(120, 165, 215),
  SEA(140, 225, 175),
  FOREST(85, 155, 60),
  SOD(170, 190, 95),
  CREAM(255, 254, 208),
  SUN(240, 225, 80),
  GOLD(255, 195, 5),
  CADMIUM(250, 105, 65),
  SANGUINE(95, 10, 0),
  MOCHA(75, 40, 0),
  MATTE(30, 30, 30),
  COBALT(0, 50, 120);

  private final String name;
  private final Color color;

  PlayerColor(int r, int g, int b) {
    this.name = toString().substring(0, 1).toUpperCase() + toString().substring(1).toLowerCase();
    this.color = new Color(r, g, b);
  }

  public String getName() {
    return name;
  }

  public Color value() {
    return color;
  }

}
