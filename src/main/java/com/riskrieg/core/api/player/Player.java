package com.riskrieg.core.api.player;

import com.riskrieg.core.constant.StandardPlayerColor;
import com.riskrieg.core.constant.color.ColorId;
import java.awt.Color;
import java.net.StandardProtocolFamily;
import java.util.Objects;

public final class Player {

  @Deprecated
  private Color color;
  private ColorId colorId;
  private final Identity identity;
  private String name;

  @Deprecated
  public Player(Identity identity, Color color, String name) {
    Objects.requireNonNull(identity);
//    Objects.requireNonNull(color);
    Objects.requireNonNull(name);
    if (name.isBlank()) {
      throw new IllegalArgumentException("name cannot be blank");
    }
    this.identity = identity;
    this.color = null;
    this.colorId = ColorId.of(StandardPlayerColor.valueOf(color).ordinal());
    this.name = name;
  }

  public Player(Identity identity, ColorId colorId, String name) {
    Objects.requireNonNull(identity);
//    Objects.requireNonNull(colorId);
    Objects.requireNonNull(name);
    if (name.isBlank()) {
      throw new IllegalArgumentException("name cannot be blank");
    }
    this.identity = identity;
    this.color = null;
    this.colorId = colorId;
    this.name = name;
  }

  @Deprecated
  public Player(Color color, String name) {
    this(Identity.random(), color, name);
  }

  public Player(ColorId colorId, String name) {
    this(Identity.random(), colorId, name);
  }

  public Identity identity() {
    return identity;
  }

  public ColorId colorId() {
    return colorId;
  }

  @Deprecated
  public Color color() {
    return color;
  }

  public String name() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Player player = (Player) o;
    return identity.equals(player.identity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(identity);
  }

}
