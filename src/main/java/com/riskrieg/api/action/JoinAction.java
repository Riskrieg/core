package com.riskrieg.api.action;

import com.riskrieg.player.Identity;
import com.riskrieg.player.Player;
import java.awt.Color;
import java.util.Collection;
import java.util.function.Consumer;
import javax.annotation.Nullable;

public final class JoinAction implements GameAction<Player> {

  private final Collection<Player> players;
  private final Identity id;
  private final String name;
  private final Color color;

  public JoinAction(Identity id, String name, Color color, Collection<Player> players) {
    this.players = players;
    this.id = id;
    this.name = name;
    this.color = color;
  }

  @Override
  public void submit(@Nullable Consumer<? super Player> success, @Nullable Consumer<? super Throwable> failure) {
    try {
      Player player = new Player(id, color, name);
      if (players.contains(player)) {
        throw new IllegalArgumentException("player is already present");
      }
      players.add(player);
      if (success != null) {
        success.accept(player);
      }
    } catch (Exception e) {
      if (failure != null) {
        failure.accept(e);
      }
    }
  }

}
