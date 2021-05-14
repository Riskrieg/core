package com.riskrieg.core.api.order;

import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.constant.StandardPlayerColor;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import javax.annotation.Nonnull;

public class StandardReversedRandomizedOrder implements TurnOrder {

  @Nonnull
  @Override
  public String displayName() {
    return "Standard (Reversed & Randomized Start)";
  }

  @Nonnull
  @Override
  public String description() {
    return "Players take turns according to their color, from bottom to top, and the player who goes first is randomized.";
  }

  @Nonnull
  @Override
  public Deque<Player> sort(@Nonnull final Collection<Player> players) {
    List<Player> playerList = new ArrayList<>(players);
    playerList.sort(new StandardComparator().reversed());
    int start = new Random().nextInt(playerList.size());
    Deque<Player> playerDeque = new ArrayDeque<>(playerList);
    for (int i = 0; i < start; i++) {
      playerDeque.addLast(playerDeque.removeFirst());
    }
    return new ArrayDeque<>(playerDeque);
  }

  private static class StandardComparator implements Comparator<Player> {

    @Override
    public int compare(Player o1, Player o2) {
      return StandardPlayerColor.valueOf(o1.color()).compareTo(StandardPlayerColor.valueOf(o2.color()));
    }

  }

}