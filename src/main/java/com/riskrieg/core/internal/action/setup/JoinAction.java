/*
 *     Riskrieg, an open-source conflict simulation game.
 *     Copyright (C) 2021  Aaron Yoder <aaronjyoder@gmail.com>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.riskrieg.core.internal.action.setup;

import com.riskrieg.core.api.gamemode.GameState;
import com.riskrieg.core.api.player.Identity;
import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.constant.Constants;
import com.riskrieg.core.constant.color.ColorId;
import com.riskrieg.core.internal.action.Action;
import java.util.Collection;
import java.util.function.Consumer;
import javax.annotation.Nullable;

public final class JoinAction implements Action<Player> {

  private final Identity id;
  private final String name;
  private final ColorId colorId;
  private final GameState gameState;
  private final Collection<Player> players;

  public JoinAction(Identity id, String name, ColorId colorId, GameState gameState, Collection<Player> players) {
    this.id = id;
    this.name = name;
    this.colorId = colorId;
    this.gameState = gameState;
    this.players = players;
  }

  @Override
  public void submit(@Nullable Consumer<? super Player> success, @Nullable Consumer<? super Throwable> failure) {
    try {
      Player player = new Player(id, colorId, name);
      switch (gameState) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case SETUP -> {
          if (players.stream().anyMatch(p -> p.colorId().equals(player.colorId()))) {
            throw new IllegalArgumentException("That color is already taken");
          }
          if (players.contains(player)) {
            throw new IllegalArgumentException("That player is already in the game");
          }
          if (players.size() >= Constants.MAX_PLAYERS) {
            throw new IllegalStateException("The game is too full to join");
          }
          players.add(player);
          if (success != null) {
            success.accept(player);
          }
        }
        default -> throw new IllegalStateException("Players can only join the game during the setup phase");
      }
    } catch (Exception e) {
      if (failure != null) {
        failure.accept(e);
      }
    }
  }

}
