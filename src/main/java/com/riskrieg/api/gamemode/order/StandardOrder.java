package com.riskrieg.api.gamemode.order;

import com.riskrieg.api.player.Player;
import java.awt.Color;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import javax.annotation.Nonnull;

public final class StandardOrder implements TurnOrder {

  @Nonnull
  @Override
  public Deque<Player> sort(@Nonnull final Collection<Player> players) {
    List<Player> playerList = new ArrayList<>(players);
    playerList.sort(new StandardComparator());
    return new ArrayDeque<>(playerList);
  }

  private static class StandardComparator implements Comparator<Player> {

    @Override
    public int compare(Player o1, Player o2) {
      return StandardColor.valueOf(o1.color()).compareTo(StandardColor.valueOf(o2.color()));
    }

  }

  private enum StandardColor {

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

    private final Color color;

    StandardColor(int r, int g, int b) {
      this.color = new Color(r, g, b);
    }

    private Color value() {
      return color;
    }

    private static StandardColor valueOf(Color color) {
      for (StandardColor sc : values()) {
        if (sc.value().equals(color)) {
          return sc;
        }
      }
      return COBALT;
    }

  }

}
