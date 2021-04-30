package com.riskrieg.core.internal.action.setup;

import com.riskrieg.core.api.nation.Nation;
import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.internal.action.Action;
import java.util.Collection;
import java.util.function.Consumer;
import javax.annotation.Nullable;

public class LeaveAction implements Action<Player> {

  private final Player player;
  private final Collection<Player> players;
  private final Collection<Nation> nations;

  public LeaveAction(Player player, Collection<Player> players, Collection<Nation> nations) {
    this.player = player;
    this.players = players;
    this.nations = nations;
  }

  @Override
  public void submit(@Nullable Consumer<? super Player> success, @Nullable Consumer<? super Throwable> failure) {
    try {
      if (player == null || !players.contains(player)) {
        throw new IllegalStateException("Player is not present");
      }
      Nation toRemove = null;
      for (Nation nation : nations) {
        if (nation.getLeaderIdentity().equals(player.identity())) {
          toRemove = nation;
        }
      }
      if (toRemove != null) { // TODO: Handle alliances
        nations.remove(toRemove);
      }
      players.remove(player);
      if (success != null) {
        success.accept(player);
      }
    } catch (Exception e) {
      if (failure != null) {
        failure.accept(e);
      }
    }
  }

}
