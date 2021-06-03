package com.riskrieg.core.internal.action;

import com.riskrieg.core.api.gamemode.GameMode;
import com.riskrieg.core.api.gamemode.GameState;
import com.riskrieg.core.api.nation.Nation;
import com.riskrieg.core.api.player.Identity;
import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.internal.GameEndReason;
import com.riskrieg.core.internal.bundle.LeaveBundle;
import java.util.Collection;
import java.util.Deque;
import java.util.function.Consumer;
import javax.annotation.Nullable;

public final class LeaveAction implements Action<LeaveBundle> {

  private final Identity identity;
  private final GameMode gameMode;
  private final Deque<Player> players;
  private final Collection<Nation> nations;

  public LeaveAction(Identity identity, GameMode gameMode, Deque<Player> players, Collection<Nation> nations) {
    this.identity = identity;
    this.gameMode = gameMode;
    this.players = players;
    this.nations = nations;
  }

  @Override
  public void submit(@Nullable Consumer<? super LeaveBundle> success, @Nullable Consumer<? super Throwable> failure) {
    try {
      var gameEndReason = GameEndReason.NONE;
      Player leavingPlayer = players.stream().filter(p -> p.identity().equals(identity)).findAny().orElse(null);
      if (leavingPlayer == null) {
        throw new IllegalStateException("Player is not present");
      }
      nations.stream().filter(n -> n.isAllied(identity)).forEach(n -> n.removeAlly(identity));
      nations.stream().filter(n -> n.identity().equals(identity)).findAny().ifPresent(nations::remove);
      players.remove(leavingPlayer);

      if (gameMode.gameState().equals(GameState.SETUP) && players.size() == 0) {
        gameMode.setGameState(GameState.ENDED);
        gameEndReason = GameEndReason.NO_PLAYERS;
      } else if ((gameMode.gameState().equals(GameState.RUNNING) || gameMode.gameState().equals(GameState.SELECTION)) && players.size() <= 1) {
        gameMode.setGameState(GameState.ENDED);
        gameEndReason = GameEndReason.FORFEIT;
      } else if (gameMode.gameState().equals(GameState.RUNNING) && nations.stream().allMatch(n -> n.allies().size() == (players.size() - 1))) {
        gameMode.setGameState(GameState.ENDED);
        gameEndReason = GameEndReason.ALLIED_VICTORY;
      }

      if (success != null) {
        Player currentTurnPlayer = players.size() > 0 ? players.getFirst() : null;
        Nation nation = currentTurnPlayer == null ? null : nations.stream().filter(n -> n.identity().equals(currentTurnPlayer.identity())).findAny().orElse(null);
        int claims = nation == null ? -1 : nation.getClaimAmount(gameMode.map(), nations);
        success.accept(new LeaveBundle(currentTurnPlayer, leavingPlayer, gameEndReason, claims));
      }
    } catch (Exception e) {
      if (failure != null) {
        failure.accept(e);
      }
    }
  }

}