package com.riskrieg.api.gamemode.order;

import com.riskrieg.api.player.Player;
import java.util.Collection;
import java.util.Deque;
import javax.annotation.Nonnull;

public interface TurnOrder {

  @Nonnull Deque<Player> order(@Nonnull final Collection<Player> players);

}
