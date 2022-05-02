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

package com.riskrieg.core.old.internal.action.running;

import com.riskrieg.core.old.api.gamemode.GameMode;
import com.riskrieg.core.old.api.gamemode.GameState;
import com.riskrieg.core.old.api.nation.Nation;
import com.riskrieg.core.old.api.player.Identity;
import com.riskrieg.core.old.api.player.Player;
import com.riskrieg.core.old.internal.GameEndReason;
import com.riskrieg.core.old.internal.action.Action;
import com.riskrieg.core.old.internal.bundle.AllianceBundle;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

public final class AllyAction implements Action<AllianceBundle> {

  private final Identity identity1;
  private final Identity identity2;
  private GameMode gameMode;
  private final GameState gameState;
  private final Collection<Player> players;
  private final Collection<Nation> nations;

  public AllyAction(Identity identity1, Identity identity2, GameMode gameMode, GameState gameState, Collection<Player> players, Collection<Nation> nations) {
    this.identity1 = identity1;
    this.identity2 = identity2;
    this.gameMode = gameMode;
    this.gameState = gameState;
    this.players = players;
    this.nations = nations;
  }

  @Override
  public void submit(@Nullable Consumer<? super AllianceBundle> success, @Nullable Consumer<? super Throwable> failure) {
    try {
      switch (gameState) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case RUNNING -> {
          Optional<Player> optPlayer1 = players.stream().filter(player -> player.identity().equals(identity1)).findAny();
          Optional<Player> optPlayer2 = players.stream().filter(player -> player.identity().equals(identity2)).findAny();
          Optional<Nation> optNation1 = nations.stream().filter(nation -> nation.identity().equals(identity1)).findAny();
          Optional<Nation> optNation2 = nations.stream().filter(nation -> nation.identity().equals(identity2)).findAny();
          if (optPlayer1.isEmpty() || optNation1.isEmpty()) {
            throw new IllegalStateException("Player is not present");
          }
          if (optPlayer2.isEmpty() || optNation2.isEmpty()) {
            throw new IllegalStateException("Player is not present");
          }
          if (identity1.equals(identity2)) {
            throw new IllegalStateException("You cannot form an alliance with yourself");
          }
          Nation nation1 = optNation1.get();
          Nation nation2 = optNation2.get();

          nation1.addAlly(nation2.identity());

          GameEndReason gameEndReason = GameEndReason.NONE;
          if (gameMode.gameState().equals(GameState.RUNNING) && nations.stream().allMatch(n -> n.allies().size() == (players.size() - 1))) {
            gameMode.setGameState(GameState.ENDED);
            gameEndReason = GameEndReason.ALLIED_VICTORY;
          }

          if (success != null) {
            success.accept(new AllianceBundle(optPlayer1.get(), optPlayer2.get(), gameEndReason));
          }
        }
        default -> throw new IllegalStateException("Alliances can only be made while the game is active");
      }
    } catch (Exception e) {
      if (failure != null) {
        failure.accept(e);
      }
    }
  }

}
