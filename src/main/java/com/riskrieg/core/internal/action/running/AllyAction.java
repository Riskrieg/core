package com.riskrieg.core.internal.action.running;

import com.riskrieg.core.api.nation.Nation;
import com.riskrieg.core.api.player.Identity;
import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.internal.action.Action;
import com.riskrieg.core.internal.bundle.AllianceBundle;
import java.util.Collection;
import java.util.function.Consumer;
import javax.annotation.Nullable;

public final class AllyAction implements Action<AllianceBundle> {

  private final Identity identity1;
  private final Identity identity2;
  private final Collection<Player> players;
  private final Collection<Nation> nations;

  public AllyAction(Identity identity1, Identity identity2, Collection<Player> players, Collection<Nation> nations) {
    this.identity1 = identity1;
    this.identity2 = identity2;
    this.players = players;
    this.nations = nations;
  }

  @Override
  public void submit(@Nullable Consumer<? super AllianceBundle> success, @Nullable Consumer<? super Throwable> failure) {
    try {
      // TODO: Implement
    } catch (Exception e) {
      if (failure != null) {
        failure.accept(e);
      }
    }
  }

}
