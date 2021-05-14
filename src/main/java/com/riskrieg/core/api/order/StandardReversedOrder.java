package com.riskrieg.core.api.order;

import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.constant.StandardPlayerColor;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import javax.annotation.Nonnull;

public class StandardReversedOrder implements TurnOrder {

  @Nonnull
  @Override
  public String displayName() {
    return "Standard (Reversed)";
  }

  @Nonnull
  @Override
  public String description() {
    return "Players take turns according to their color, from bottom to top.";
  }

  @Nonnull
  @Override
  public Deque<Player> sort(@Nonnull final Collection<Player> players) {
    List<Player> playerList = new ArrayList<>(players);
    playerList.sort(new StandardComparator().reversed());
    return new ArrayDeque<>(playerList);
  }

  private static class StandardComparator implements Comparator<Player> {

    @Override
    public int compare(Player o1, Player o2) {
      return StandardPlayerColor.valueOf(o1.color()).compareTo(StandardPlayerColor.valueOf(o2.color()));
    }

  }

}