package com.riskrieg.core.internal.action;

import com.riskrieg.core.api.Riskrieg;
import com.riskrieg.core.gamemode.GameMode;
import com.riskrieg.core.gamemode.GameState;
import com.riskrieg.core.map.GameMap;
import com.riskrieg.core.nation.Nation;
import com.riskrieg.core.player.Player;
import java.util.Collection;
import java.util.function.Consumer;
import javax.annotation.Nullable;

public final class StartAction implements GameAction<GameState> {

  private final GameMode gameMode;
  private final GameMap gameMap;
  private final Collection<Player> players;
  private final Collection<Nation> nations;

  public StartAction(GameMode gameMode, GameMap gameMap, Collection<Player> players, Collection<Nation> nations) {
    this.gameMode = gameMode;
    this.gameMap = gameMap;
    this.players = players;
    this.nations = nations;
  }

  @Override
  public void submit(@Nullable Consumer<? super GameState> success, @Nullable Consumer<? super Throwable> failure) {
    try {
      switch (gameMode.gameState()) {
        case ENDED, RUNNING -> throw new IllegalStateException("The map can only be set during the setup phase");
        case SETUP -> {
          if (players.size() < Riskrieg.MIN_PLAYERS) {
            throw new IllegalStateException("Two or more players are required to play");
          }
          if (!gameMap.isSet()) {
            throw new IllegalStateException("A valid map must be selected before starting the game");
          }
          if (nations.size() < players.size()) {
            throw new IllegalStateException("Not all players have selected a capital");
          }
          if (nations.size() > players.size()) {
            throw new IllegalStateException("Critical error: game cannot be started and must be reset");
          }
          gameMode.setGameState(GameState.RUNNING);
          if (success != null) {
            success.accept(gameMode.gameState());
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