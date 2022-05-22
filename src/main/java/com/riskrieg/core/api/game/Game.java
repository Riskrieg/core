/*
 *     Riskrieg, an open-source conflict simulation game.
 *     Copyright (C) 2021 Aaron Yoder <aaronjyoder@gmail.com> and Contributors
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

package com.riskrieg.core.api.game;

import com.riskrieg.core.api.color.ColorBatch;
import com.riskrieg.core.api.color.GameColor;
import com.riskrieg.core.api.game.entity.nation.Nation;
import com.riskrieg.core.api.game.entity.player.Player;
import com.riskrieg.core.api.game.map.GameMap;
import com.riskrieg.core.api.game.map.Options;
import com.riskrieg.core.api.game.map.options.Availability;
import com.riskrieg.core.api.identifier.GameIdentifier;
import com.riskrieg.core.api.identifier.NationIdentifier;
import com.riskrieg.core.api.identifier.PlayerIdentifier;
import com.riskrieg.core.api.identifier.TerritoryIdentifier;
import com.riskrieg.core.api.requests.GameAction;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.time.Instant;
import java.util.Deque;
import java.util.Optional;
import java.util.Set;

public interface Game {

  GameIdentifier identifier();

  GameConstants constants();

  ColorBatch colors();

  @NonNull
  Instant creationTime();

  @NonNull
  Instant updatedTime();

  @NonNull
  GameState state();

  GameMap map();

  @NonNull
  Set<Nation> nations();

  @NonNull
  Deque<Player> players();

  default Optional<Player> getPlayer(PlayerIdentifier identifier) {
    return players().stream()
        .filter(p -> p.id().equals(identifier))
        .findFirst();
  }

  default Optional<Nation> getNation(PlayerIdentifier identifier) {
    return nations().stream()
        .filter(n -> n.leaderIdentifier().equals(identifier))
        .findFirst();
  }

  default Optional<Nation> getNation(NationIdentifier identifier) {
    return nations().stream()
        .filter(n -> n.identifier().equals(identifier))
        .findFirst();
  }

  default Optional<Nation> getNation(GameColor color) {
    return nations().stream()
        .filter(n -> n.colorId() == color.id())
        .findFirst();
  }

  GameAction<Boolean> setState(GameState state);

  GameAction<GameMap> selectMap(GameMap map);

  GameAction<Player> addPlayer(PlayerIdentifier identifier, String name);

  GameAction<?> removePlayer(PlayerIdentifier identifier);

  GameAction<Nation> createNation(GameColor color, PlayerIdentifier identifier);

  GameAction<?> addTerritory(NationIdentifier nation, TerritoryIdentifier territory, TerritoryIdentifier... territories); // TODO: Replace <?>

  GameAction<?> removeTerritory(NationIdentifier nation, TerritoryIdentifier territory, TerritoryIdentifier... territories); // TODO: Replace <?>

  GameAction<Player> start();

  GameAction<?> skip(PlayerIdentifier identifier);

  GameAction<?> claim();

}
