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
import com.riskrieg.core.api.map.GameMap;
import com.riskrieg.core.api.map.GameTerritory;
import com.riskrieg.core.api.map.TerritoryType;
import com.riskrieg.core.api.nation.Nation;
import com.riskrieg.core.api.player.Identity;
import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.internal.action.Action;
import com.riskrieg.map.territory.TerritoryId;
import java.util.Collection;
import java.util.Deque;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

public final class FormNationAction implements Action<Nation> {

  private final Identity identity;
  private final TerritoryId id;
  private final TerritoryType firstTerritoryType;
  private final GameState gameState;
  private final GameMap gameMap;
  private final Collection<Player> players;
  private final Collection<Nation> nations;

  public FormNationAction(Identity identity, TerritoryId id, TerritoryType firstTerritoryType, GameState gameState, GameMap gameMap,
      Collection<Player> players, Collection<Nation> nations) {
    this.identity = identity;
    this.id = id;
    this.firstTerritoryType = firstTerritoryType;
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
        case SETUP -> {
          if (players.stream().noneMatch(p -> p.identity().equals(identity))) {
            throw new IllegalStateException("Player is not present");
          }
          if (!gameMap.isSet()) {
            throw new IllegalStateException("A valid map must be selected before selecting a capital");
          }
          if (gameMap.graph().vertexSet().stream().noneMatch(t -> t.id().equals(id))) {
            throw new IllegalStateException("No such territory exists on the selected map");
          }
          if (nations.stream().anyMatch(n -> n.identity().equals(identity))) {
            throw new IllegalStateException("That player has already selected a capital");
          }
          var territoryIds = nations.stream().map(Nation::territories).flatMap(Set::stream).collect(Collectors.toSet());
          if (territoryIds.contains(id)) {
            throw new IllegalStateException("That territory is already taken by someone else");
          }
          Nation nation = new Nation(identity, new GameTerritory(id, firstTerritoryType));
          nations.add(nation);
          if (success != null) {
            success.accept(nation);
          }
        }
        default -> throw new IllegalStateException("Capitals can only be selected during the setup phase");
      }
    } catch (Exception e) {
      if (failure != null) {
        failure.accept(e);
      }
    }
  }

}
