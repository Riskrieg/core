package com.riskrieg.core.api.order;

import com.riskrieg.core.api.player.Player;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;

public final class FullRandomOrder implements TurnOrder {

  @Nonnull
  @Override
  public String displayName() {
    return "Full Random";
  }

  @Nonnull
  @Override
  public String description() {
    return "Players take turns in an order that is randomized at the start of the game, and the player who goes first is randomized.";
  }

  @Nonnull
  @Override
  public Deque<Player> sort(@Nonnull final Collection<Player> players) {
    Objects.requireNonNull(players);
    List<Player> playerList = new ArrayList<>(players);
    Collections.shuffle(playerList);
    return new ArrayDeque<>(playerList);
  }

}