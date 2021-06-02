package com.riskrieg.core.internal.action.running;

import com.riskrieg.core.api.gamemode.GameState;
import com.riskrieg.core.api.map.GameMap;
import com.riskrieg.core.api.nation.Nation;
import com.riskrieg.core.api.player.Identity;
import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.internal.action.Action;
import com.riskrieg.core.internal.bundle.SkipBundle;
import java.util.Collection;
import java.util.Deque;
import java.util.function.Consumer;
import javax.annotation.Nullable;

public final class SkipAction implements Action<SkipBundle> {

  private final Identity identity;
  private final GameState gameState;
  private final GameMap gameMap;
  private final Deque<Player> players;
  private final Collection<Nation> nations;
  private final boolean skipSelf;

  public SkipAction(Identity identity, GameState gameState, GameMap gameMap, Deque<Player> players, Collection<Nation> nations) {
    this.identity = identity;
    this.gameState = gameState;
    this.gameMap = gameMap;
    this.players = players;
    this.nations = nations;
    this.skipSelf = false;
  }

  public SkipAction(Identity identity, GameState gameState, GameMap gameMap, Deque<Player> players, Collection<Nation> nations, boolean skipSelf) {
    this.identity = identity;
    this.gameState = gameState;
    this.gameMap = gameMap;
    this.players = players;
    this.nations = nations;
    this.skipSelf = skipSelf;
  }

  @Override
  public void submit(@Nullable Consumer<? super SkipBundle> success, @Nullable Consumer<? super Throwable> failure) {
    try {
      switch (gameState) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case RUNNING -> {
          Player playerUsingSkip = players.stream().filter(p -> p.identity().equals(identity)).findAny().orElse(null);
          if (playerUsingSkip == null) {
            throw new IllegalStateException("Player is not present");
          }

          if (skipSelf && !playerUsingSkip.identity().equals(players.getFirst().identity())) {
            throw new IllegalStateException("Cannot skip another player's turn");
          }

          players.addLast(players.removeFirst());
          if (success != null) {
            Player currentTurnPlayer = players.size() > 0 ? players.getFirst() : null;
            Nation nation = currentTurnPlayer == null ? null : nations.stream().filter(n -> n.identity().equals(currentTurnPlayer.identity())).findAny().orElse(null);
            int claims = nation == null ? -1 : nation.getClaimAmount(gameMap, nations);
            success.accept(new SkipBundle(currentTurnPlayer, playerUsingSkip, claims));
          }
        }
        default -> throw new IllegalStateException("Turns can only be skipped while the game is active");
      }
    } catch (Exception e) {
      if (failure != null) {
        failure.accept(e);
      }
    }
  }

}
