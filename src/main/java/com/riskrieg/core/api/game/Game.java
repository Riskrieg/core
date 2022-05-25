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
import com.riskrieg.core.api.game.order.TurnOrder;
import com.riskrieg.core.api.game.territory.Claim;
import com.riskrieg.core.api.game.territory.GameTerritory;
import com.riskrieg.core.api.identifier.GameIdentifier;
import com.riskrieg.core.api.identifier.NationIdentifier;
import com.riskrieg.core.api.identifier.PlayerIdentifier;
import com.riskrieg.core.api.requests.GameAction;
import edu.umd.cs.findbugs.annotations.CheckReturnValue;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface Game {

  @NonNull
  GameIdentifier identifier();

  @NonNull
  GameConstants constants();

  @NonNull
  ColorBatch colors();

  @NonNull
  Instant creationTime();

  @NonNull
  Instant updatedTime();

  @NonNull
  GamePhase phase();

  @Nullable
  GameMap map();

  @NonNull
  Collection<Player> players();

  @NonNull
  Set<Nation> nations();

  @NonNull
  Set<Claim> claims();

  @NonNull
  default Optional<Player> getPlayer(PlayerIdentifier identifier) {
    return players().stream()
        .filter(p -> p.identifier().equals(identifier))
        .findFirst();
  }

  @NonNull
  default Optional<Nation> getNation(PlayerIdentifier identifier) {
    return nations().stream()
        .filter(n -> n.leaderIdentifier().equals(identifier))
        .findFirst();
  }

  @NonNull
  default Optional<Nation> getNation(NationIdentifier identifier) {
    return nations().stream()
        .filter(n -> n.identifier().equals(identifier))
        .findFirst();
  }

  @NonNull
  default Optional<Nation> getNation(GameColor color) {
    return nations().stream()
        .filter(n -> n.colorId() == color.id())
        .findFirst();
  }

  @NonNull
  @CheckReturnValue
  GameAction<GameMap> selectMap(GameMap map);

  @NonNull
  @CheckReturnValue
  GameAction<Player> addPlayer(PlayerIdentifier identifier, String name);

  @NonNull
  @CheckReturnValue
  GameAction<Boolean> removePlayer(PlayerIdentifier identifier);

  @NonNull
  @CheckReturnValue
  GameAction<Nation> createNation(GameColor color, PlayerIdentifier identifier);

  @NonNull
  @CheckReturnValue
  GameAction<Boolean> claim(NationIdentifier identifier, GameTerritory territory, GameTerritory... territories);

  @NonNull
  @CheckReturnValue
  GameAction<Boolean> unclaim(NationIdentifier identifier, GameTerritory territory, GameTerritory... territories);

  @NonNull
  @CheckReturnValue
  GameAction<Player> start(TurnOrder order);

  @NonNull
  @CheckReturnValue
  GameAction<Boolean> advanceTurn();

}
