package com.riskrieg.core.internal.action.running;

import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.internal.action.Action;
import com.riskrieg.core.internal.bundle.UpdateBundle;
import com.riskrieg.core.unsorted.gamemode.GameState;
import java.util.Deque;
import java.util.function.Consumer;
import javax.annotation.Nullable;

public class UpdateAction implements Action<UpdateBundle> {

  private final GameState gameState;
  private final Deque<Player> players;

  public UpdateAction(GameState gameState, Deque<Player> players) {
    this.gameState = gameState;
    this.players = players;
  }

  @Override
  public void submit(@Nullable Consumer<? super UpdateBundle> success, @Nullable Consumer<? super Throwable> failure) {
    try {
      switch (gameState) {
        case ENDED, SETUP -> throw new IllegalStateException("Attempted to update turn in invalid game state");
        case RUNNING -> {
          players.addLast(players.removeFirst());
          if (success != null) {
            success.accept(new UpdateBundle(players.getFirst(), gameState));
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
