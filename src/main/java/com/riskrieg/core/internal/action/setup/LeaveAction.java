package com.riskrieg.core.internal.action.setup;

import com.riskrieg.core.api.nation.Nation;
import com.riskrieg.core.api.player.Identity;
import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.internal.action.Action;
import java.util.Collection;
import java.util.function.Consumer;
import javax.annotation.Nullable;

public final class LeaveAction implements Action<Player> {

  private final Identity identity;
  private final Collection<Player> players;
  private final Collection<Nation> nations;

  public LeaveAction(Identity identity, Collection<Player> players, Collection<Nation> nations) {
    this.identity = identity;
    this.players = players;
    this.nations = nations;
  }

  @Override
  public void submit(@Nullable Consumer<? super Player> success, @Nullable Consumer<? super Throwable> failure) {
    try {
      Player playerToRemove = players.stream().filter(p -> p.identity().equals(identity)).findAny().orElse(null);
      if (playerToRemove == null) {
        throw new IllegalStateException("Player is not present");
      }
      nations.stream().filter(n -> n.getLeaderIdentity().equals(identity)).findAny().ifPresent(nations::remove);
      players.remove(playerToRemove);
      if (success != null) {
        success.accept(playerToRemove);
      }
    } catch (Exception e) {
      if (failure != null) {
        failure.accept(e);
      }
    }
  }

}
