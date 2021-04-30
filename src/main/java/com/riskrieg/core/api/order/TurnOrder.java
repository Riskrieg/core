package com.riskrieg.core.api.order;

import com.riskrieg.core.api.player.Player;
import java.util.Collection;
import java.util.Deque;
import javax.annotation.Nonnull;

public interface TurnOrder {

  /**
   * This method should always return a Deque that has the same elements, and number of elements, as the input Collection, regardless of implementation.
   *
   * @param players The collection of players to sort.
   * @return A Deque of players sorted based on the implementation.
   */
  @Nonnull
  Deque<Player> sort(@Nonnull final Collection<Player> players);

}
