package com.riskrieg.core.internal.action;

import com.riskrieg.core.api.Riskrieg;
import com.riskrieg.core.constant.Constants;
import com.riskrieg.core.gamemode.GameState;
import com.riskrieg.core.player.Identity;
import com.riskrieg.core.player.Player;
import java.awt.Color;
import java.util.Collection;
import java.util.function.Consumer;
import javax.annotation.Nullable;

public final class JoinAction implements GameAction<Player> {

  private final Identity id;
  private final String name;
  private final Color color;
  private final GameState gameState;
  private final Collection<Player> players;

  public JoinAction(Identity id, String name, Color color,GameState gameState, Collection<Player> players) {
    this.id = id;
    this.name = name;
    this.color = color;
    this.gameState = gameState;
    this.players = players;
  }

  @Override
  public void submit(@Nullable Consumer<? super Player> success, @Nullable Consumer<? super Throwable> failure) {
    try {
      Player player = new Player(id, color, name);
      switch (gameState) {
        case ENDED, RUNNING -> throw new IllegalStateException("Players can only join the game during the setup phase");
        case SETUP -> {
          if (players.contains(player) || players.stream().anyMatch(p -> p.identity().equals(player.identity()))) {
            throw new IllegalArgumentException("Player is already present or color already taken");
          }
          if (players.size() >= Riskrieg.MAX_PLAYERS) {
            throw new IllegalStateException("The game is too full to join");
          }
          players.add(player);
          if (success != null) {
            success.accept(player);
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
