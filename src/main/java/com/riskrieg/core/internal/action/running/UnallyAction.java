package com.riskrieg.core.internal.action.running;

import com.riskrieg.core.api.nation.Nation;
import com.riskrieg.core.api.player.Identity;
import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.internal.action.Action;
import com.riskrieg.core.internal.bundle.AllianceBundle;
import com.riskrieg.core.unsorted.gamemode.GameState;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
import javax.annotation.Nullable;

public final class UnallyAction implements Action<AllianceBundle> {

  private final Identity identity1;
  private final Identity identity2;
  private final GameState gameState;
  private final Collection<Player> players;
  private final Collection<Nation> nations;

  public UnallyAction(Identity identity1, Identity identity2, GameState gameState, Collection<Player> players, Collection<Nation> nations) {
    this.identity1 = identity1;
    this.identity2 = identity2;
    this.gameState = gameState;
    this.players = players;
    this.nations = nations;
  }

  @Override
  public void submit(@Nullable Consumer<? super AllianceBundle> success, @Nullable Consumer<? super Throwable> failure) {
    try {
      switch (gameState) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case SETUP -> throw new IllegalStateException("Alliances can only be broken while the game is active");
        case RUNNING -> {
          Optional<Player> optPlayer1 = players.stream().filter(player -> player.identity().equals(identity1)).findAny();
          Optional<Player> optPlayer2 = players.stream().filter(player -> player.identity().equals(identity2)).findAny();
          Optional<Nation> optNation1 = nations.stream().filter(nation -> nation.identity().equals(identity1)).findAny();
          Optional<Nation> optNation2 = nations.stream().filter(nation -> nation.identity().equals(identity2)).findAny();
          if (optPlayer1.isEmpty() || optNation1.isEmpty()) {
            throw new IllegalStateException("Player is not present");
          }
          if (optPlayer2.isEmpty() || optNation2.isEmpty()) {
            throw new IllegalStateException("Player is not present");
          }
          if (identity1.equals(identity2)) {
            throw new IllegalStateException("You cannot break an alliance with yourself");
          }
          Nation nation1 = optNation1.get();
          Nation nation2 = optNation2.get();

          nation1.removeAlly(nation2.identity());
          nation2.removeAlly(nation1.identity());
          if (success != null) {
            success.accept(new AllianceBundle(optPlayer1.get(), optPlayer2.get()));
          }
        }
      }
    } catch (Exception e) {
      if (failure != null) {
        failure.accept(e);
      }
    }
  }

}
