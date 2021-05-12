package com.riskrieg.core.internal.action.running;

import com.riskrieg.core.api.gamemode.GameMode;
import com.riskrieg.core.api.nation.Nation;
import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.internal.GameEndReason;
import com.riskrieg.core.internal.action.Action;
import com.riskrieg.core.internal.bundle.UpdateBundle;
import com.riskrieg.core.unsorted.gamemode.GameState;
import com.riskrieg.core.unsorted.map.GameMap;
import com.riskrieg.core.unsorted.map.TerritoryType;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import javax.annotation.Nullable;

public class RegicideUpdateAction implements Action<UpdateBundle> {

  private final GameMode gameMode;
  private final GameState gameState;
  private final GameMap gameMap;
  private final Deque<Player> players;
  private final Collection<Nation> nations;

  public RegicideUpdateAction(GameMode gameMode, GameState gameState, GameMap gameMap, Deque<Player> players, Collection<Nation> nations) {
    this.gameMode = gameMode;
    this.gameState = gameState;
    this.gameMap = gameMap;
    this.players = players;
    this.nations = nations;
  }

  @Override
  public void submit(@Nullable Consumer<? super UpdateBundle> success, @Nullable Consumer<? super Throwable> failure) {
    try {
      switch (gameState) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case SETUP -> throw new IllegalStateException("Attempted to update turn in invalid game state");
        case RUNNING -> {
          var gameEndReason = GameEndReason.NONE;

          /* Defeated Check */
          Set<Player> defeated = new HashSet<>();
          for (Nation nation : nations) {
            if (nation.territories().size() == 0 || nation.territories().stream().noneMatch(tid -> nation.territoryIsOfType(tid, TerritoryType.CAPITAL))) {
              players.stream().filter(p -> p.identity().equals(nation.identity())).findAny().ifPresent(defeated::add);
            }
          }
          defeated.forEach(p -> gameMode.leave(p.identity()).submit());

          /* End State Check */
          if (players.size() == 0) {
            gameEndReason = GameEndReason.NO_PLAYERS;
          } else if (players.size() == 1) {
            gameMode.setGameState(GameState.ENDED);
            gameEndReason = GameEndReason.DEFEAT;
          } else if (nations.stream().allMatch(n -> n.getClaimAmount(gameMap, nations) == 0)) {
            gameMode.setGameState(GameState.ENDED);
            gameEndReason = GameEndReason.STALEMATE;
          }
          Player previousPlayer = players.size() == 0 ? null : players.getFirst();
          players.addLast(players.removeFirst());
          if (success != null) {
            Player currentTurnPlayer = players.size() > 0 ? players.getFirst() : null;
            Nation nation = currentTurnPlayer == null ? null : nations.stream().filter(n -> n.identity().equals(currentTurnPlayer.identity())).findAny().orElse(null);
            int claims = nation == null ? -1 : nation.getClaimAmount(gameMode.map(), nations);
            success.accept(new UpdateBundle(previousPlayer, currentTurnPlayer, gameEndReason, claims, defeated));
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
