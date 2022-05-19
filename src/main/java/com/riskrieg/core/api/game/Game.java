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

import com.riskrieg.core.api.color.GameColor;
import com.riskrieg.core.api.game.entity.nation.Nation;
import com.riskrieg.core.api.game.entity.player.Player;
import com.riskrieg.core.api.game.map.GameMap;
import com.riskrieg.core.api.game.map.Options;
import com.riskrieg.core.api.identifier.GameIdentifier;
import com.riskrieg.core.api.identifier.PlayerIdentifier;
import com.riskrieg.core.api.identifier.TerritoryIdentifier;
import com.riskrieg.core.api.requests.GameAction;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;

public interface Game {

  // TODO: Load these values from a file on start-up
  public static final int MIN_PLAYERS = 2;
  public static final int MAX_PLAYERS = 16;
  public static final double CLAIM_INCREASE_THRESHOLD = 5.0; // Threshold to gain another claim each turn
  public static final int MINIMUM_CLAIM_AMOUNT = 1;
  public static final int CAPITAL_ATTACK_ROLL_BOOST = 2;
  public static final int CAPITAL_DEFENSE_ROLL_BOOST = 1;

  GameIdentifier identifier();

  @NonNull
  GameMode mode();

  @NonNull
  Instant creationTime();

  @NonNull
  Instant updatedTime();

  @NonNull
  GameState gameState();

  Set<Nation> nations();

  Set<Player> players();

  GameMap map();

  default Optional<Player> getPlayer(PlayerIdentifier identifier) {
    for (Player player : players()) {
      if (player.id().equals(identifier)) {
        return Optional.of(player);
      }
    }
    return Optional.empty();
  }

  default Optional<Nation> getNation(PlayerIdentifier identifier) {
    for (Nation nation : nations()) {
      if (nation.players().stream().anyMatch(pid -> pid.equals(identifier))) {
        return Optional.of(nation);
      }
    }
    return Optional.empty();
  }

  default Optional<Nation> getNation(GameColor color) {
    for (Nation nation : nations()) {
      if (nation.color().equals(color)) {
        return Optional.of(nation);
      }
    }
    return Optional.empty();
  }

  GameAction<Boolean> setGameState(GameState gameState);

  GameAction<Player> addPlayer(PlayerIdentifier identifier, String name);

  GameAction<?> removePlayer(PlayerIdentifier identifier);

  GameAction<GameMap> selectMap(GameMap map, Options options);

  GameAction<Nation> formNation(GameColor color, PlayerIdentifier identifier); // Select color and starting territory

  GameAction<?> addTerritory(Nation nation, TerritoryIdentifier territory, TerritoryIdentifier... territories); // TODO: Replace Nation with NationIdentifier?

  GameAction<?> removeTerritory(Nation nation, TerritoryIdentifier territory, TerritoryIdentifier... territories); // TODO: Replace Nation with NationIdentifier?

  GameAction<Player> start();

  GameAction<?> skip(PlayerIdentifier identifier);

  GameAction<?> claim();

}
