package com.riskrieg.core.api.order;

import com.riskrieg.core.api.player.Player;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
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

}