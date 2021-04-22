package com.riskrieg.api.action;

import com.riskrieg.constant.Constants;
import com.riskrieg.gamemode.GameState;
import com.riskrieg.player.Identity;
import com.riskrieg.player.Player;
import java.awt.Color;
import java.util.Collection;
import java.util.function.Consumer;
import javax.annotation.Nullable;

public final class JoinAction implements GameAction<Player> {

  private final Collection<Player> players;
  private final GameState gameState;
  private final Identity id;
  private final String name;
  private final Color color;

  public JoinAction(Identity id, String name, Color color, Collection<Player> players, GameState gameState) {
    this.id = id;
    this.name = name;
    this.color = color;
    this.players = players;
    this.gameState = gameState;
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
          if (players.size() >= Constants.MAX_PLAYERS) {
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
