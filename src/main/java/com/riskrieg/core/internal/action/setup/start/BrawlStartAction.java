package com.riskrieg.core.internal.action.setup.start;

import com.riskrieg.core.api.gamemode.GameMode;
import com.riskrieg.core.api.gamemode.GameState;
import com.riskrieg.core.api.map.GameMap;
import com.riskrieg.core.api.nation.Nation;
import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.constant.Constants;
import com.riskrieg.core.internal.action.Action;
import java.util.Collection;
import java.util.Deque;
import java.util.function.Consumer;
import javax.annotation.Nullable;

public class BrawlStartAction implements Action<Player> {

  private final GameMode gameMode;
  private final GameMap gameMap;
  private final Deque<Player> players;
  private final Collection<Nation> nations;

  public BrawlStartAction(GameMode gameMode, GameMap gameMap, Deque<Player> players, Collection<Nation> nations) {
    this.gameMode = gameMode;
    this.gameMap = gameMap;
    this.players = players;
    this.nations = nations;
  }

  @Override
  public void submit(@Nullable Consumer<? super Player> success, @Nullable Consumer<? super Throwable> failure) {
    try {
      switch (gameMode.gameState()) {
        default -> throw new IllegalStateException("The map can only be set during the setup phase");
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case SETUP -> {
          if (players.size() < Constants.MIN_PLAYERS) {
            throw new IllegalStateException("Two or more players are required to play");
          }
          if (!gameMap.isSet()) {
            throw new IllegalStateException("A valid map must be selected before starting the game");
          }
          if (nations.size() > players.size()) {
            throw new IllegalStateException("Critical error: game cannot be started");
          }
          gameMode.setGameState(GameState.SELECTION);
          if (success != null) {
            success.accept(players.getFirst());
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

