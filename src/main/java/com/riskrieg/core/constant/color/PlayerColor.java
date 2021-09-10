package com.riskrieg.core.constant.color;

import java.awt.Color;
import java.util.Objects;

public class PlayerColor implements Comparable<PlayerColor> {

  private final ColorId id;
  private final String name;
  private final int r;
  private final int g;
  private final int b;

  public PlayerColor(ColorId id, String name, int r, int g, int b) {
    if (name.isBlank()) {
      throw new IllegalStateException("PlayerColor name cannot be blank");
    }
    if (r < 0 || r > 255) {
      throw new IllegalStateException("PlayerColor value for red must be between 0 and 255, inclusive on either end.");
    }
    if (g < 0 || g > 255) {
      throw new IllegalStateException("PlayerColor value for green must be between 0 and 255, inclusive on either end.");
    }
    if (b < 0 || b > 255) {
      throw new IllegalStateException("PlayerColor value for blue must be between 0 and 255, inclusive on either end.");
    }
    this.id = id;
    this.name = name;
    this.r = r;
    this.g = g;
    this.b = b;
  }

  public ColorId id() {
    return id;
  }

  public String name() {
    return name;
  }

  public Color color() {
    return new Color(r, g, b);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerColor that = (PlayerColor) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public int compareTo(PlayerColor o) {
    return this.id.compareTo(o.id);
  }

}
