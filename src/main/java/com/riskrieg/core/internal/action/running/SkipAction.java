/**
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

package com.riskrieg.core.internal.action.running;

import com.riskrieg.core.api.gamemode.GameState;
import com.riskrieg.core.api.map.GameMap;
import com.riskrieg.core.api.nation.Nation;
import com.riskrieg.core.api.player.Identity;
import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.internal.action.Action;
import com.riskrieg.core.internal.bundle.SkipBundle;
import java.util.Collection;
import java.util.Deque;
import java.util.function.Consumer;
import javax.annotation.Nullable;

public final class SkipAction implements Action<SkipBundle> {

  private final Identity identity;
  private final GameState gameState;
  private final GameMap gameMap;
  private final Deque<Player> players;
  private final Collection<Nation> nations;
  private final boolean skipSelf;

  public SkipAction(Identity identity, GameState gameState, GameMap gameMap, Deque<Player> players, Collection<Nation> nations) {
    this.identity = identity;
    this.gameState = gameState;
    this.gameMap = gameMap;
    this.players = players;
    this.nations = nations;
    this.skipSelf = false;
  }

  public SkipAction(Identity identity, GameState gameState, GameMap gameMap, Deque<Player> players, Collection<Nation> nations, boolean skipSelf) {
    this.identity = identity;
    this.gameState = gameState;
    this.gameMap = gameMap;
    this.players = players;
    this.nations = nations;
    this.skipSelf = skipSelf;
  }

  @Override
  public void submit(@Nullable Consumer<? super SkipBundle> success, @Nullable Consumer<? super Throwable> failure) {
    try {
      switch (gameState) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case RUNNING, SELECTION -> {
          if (skipSelf) {
            Player playerUsingSkip = players.stream().filter(p -> p.identity().equals(identity)).findAny().orElse(null);
            if (playerUsingSkip == null) {
              throw new IllegalStateException("Player is not present");
            } else if (!playerUsingSkip.identity().equals(players.getFirst().identity())) {
              throw new IllegalStateException("Cannot skip another player's turn");
            }
          }

          Player skippedPlayer = players.getFirst();

          players.addLast(players.removeFirst());
          if (success != null) {
            Player currentTurnPlayer = players.size() > 0 ? players.getFirst() : null;
            Nation nation = currentTurnPlayer == null ? null : nations.stream().filter(n -> n.identity().equals(currentTurnPlayer.identity())).findAny().orElse(null);
            int claims = nation == null ? -1 : nation.getClaimAmount(gameMap, nations);
            success.accept(new SkipBundle(currentTurnPlayer, skippedPlayer, claims));
          }
        }
        default -> throw new IllegalStateException("Turns can only be skipped while the game is active");
      }
    } catch (Exception e) {
      if (failure != null) {
        failure.accept(e);
      }
    }
  }

}
