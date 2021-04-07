package com.riskrieg.api.gamemode.order;

import com.riskrieg.api.player.Player;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;

public class RandomOrder implements TurnOrder {

  @Nonnull
  @Override
  public Deque<Player> order(@Nonnull final Collection<Player> players) {
    Objects.requireNonNull(players);
    List<Player> playerList = new ArrayList<>(players);
    Collections.shuffle(playerList);
    return new ArrayDeque<>(playerList);
  }

}
