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

import com.riskrieg.core.old.api.gamemode.GameState;
import com.riskrieg.core.old.api.map.GameMap;
import com.riskrieg.core.old.api.map.GameTerritory;
import com.riskrieg.core.old.api.map.TerritoryType;
import com.riskrieg.core.old.api.nation.Nation;
import com.riskrieg.core.old.api.player.Identity;
import com.riskrieg.core.old.api.player.Player;
import com.riskrieg.core.old.internal.action.Action;
import com.riskrieg.map.territory.TerritoryId;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.util.Collection;
import java.util.Deque;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class SelectTerritoryAction implements Action<Nation> { // Meant for Brawl Mode only

  private final Identity identity;
  private final TerritoryId id;
  private final TerritoryType territoryType;
  private final GameState gameState;
  private final GameMap gameMap;
  private final Deque<Player> players;
  private final Collection<Nation> nations;

  public SelectTerritoryAction(Identity identity, TerritoryId id, TerritoryType territoryType, GameState gameState, GameMap gameMap,
      Deque<Player> players, Collection<Nation> nations) {
    this.identity = identity;
    this.id = id;
    this.territoryType = territoryType;
    this.gameState = gameState;
    this.gameMap = gameMap;
    this.players = players;
    this.nations = nations;
  }

  @Override
  public void submit(@Nullable Consumer<? super Nation> success, @Nullable Consumer<? super Throwable> failure) {
    try {
      switch (gameState) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case SETUP, RUNNING -> throw new IllegalStateException("Territories can only be selected during the selection phase");
        case SELECTION -> {
          if (players.stream().noneMatch(p -> p.identity().equals(identity))) {
            throw new IllegalStateException("Player is not present");
          }
          if (!players.getFirst().identity().equals(identity)) {
            throw new IllegalStateException("It is not that player's turn");
          }
          if (gameMap.graph().vertexSet().stream().noneMatch(t -> t.id().equals(id))) {
            throw new IllegalStateException("No such territory exists on the map"); // TODO: Say which territory
          }
          var territoryIds = nations.stream().map(Nation::territories).flatMap(Set::stream).collect(Collectors.toSet());
          if (territoryIds.contains(id)) {
            throw new IllegalStateException("That territory is already taken");
          }
          Nation nation = nations.stream().filter(n -> n.identity().equals(identity)).findAny().orElse(null);
          if (nation == null) {
            nation = new Nation(identity, new GameTerritory(id, territoryType));
            nations.add(nation);
          } else {
            nation.add(id, territoryType);
          }
          if (success != null) {
            success.accept(nation);
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
