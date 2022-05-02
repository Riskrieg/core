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

package com.riskrieg.core.old.api.gamemode;

import com.riskrieg.core.old.api.map.GameMap;
import com.riskrieg.core.old.api.map.MapOptions;
import com.riskrieg.core.old.api.nation.Nation;
import com.riskrieg.core.old.api.order.TurnOrder;
import com.riskrieg.core.old.api.player.Identity;
import com.riskrieg.core.old.api.player.Player;
import com.riskrieg.core.old.constant.color.ColorId;
import com.riskrieg.core.old.constant.color.PlayerColor;
import com.riskrieg.core.old.internal.action.Action;
import com.riskrieg.core.old.internal.action.GenericAction;
import com.riskrieg.core.old.internal.bundle.ClaimBundle;
import com.riskrieg.core.old.internal.bundle.CurrentStateBundle;
import com.riskrieg.core.old.internal.bundle.LeaveBundle;
import com.riskrieg.core.old.internal.bundle.SkipBundle;
import com.riskrieg.core.old.internal.bundle.UpdateBundle;
import com.riskrieg.map.RkmMap;
import com.riskrieg.map.territory.TerritoryId;
import edu.umd.cs.findbugs.annotations.CheckReturnValue;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.time.Instant;
import java.util.Collection;

public interface GameMode {

  @NonNull
  String displayName();

  @NonNull
  GameID id();

  @NonNull
  Instant creationTime();

  @NonNull
  Instant lastUpdated();

  @NonNull
  GameState gameState();

  void setGameState(@NonNull GameState gameState);

  boolean isEnded();

  @NonNull
  default Player getPlayer(@NonNull Identity identity) {
    for (Player player : players()) {
      if (player.identity().equals(identity)) {
        return player;
      }
    }
    return null;
  }

  @Nullable
  default Player getPlayer(@NonNull PlayerColor playerColor) {
    for (Player player : players()) {
      if (player.colorId().equals(playerColor.id())) {
        return player;
      }
    }
    return null;
  }

  @Nullable
  default Nation getNation(@NonNull Identity identity) {
    for (Nation nation : nations()) {
      if (nation.identity().equals(identity)) {
        return nation;
      }
    }
    return null;
  }

  @Nullable
  default Nation getNation(@NonNull PlayerColor playerColor) {
    Player leader = getPlayer(playerColor);
    if (leader != null) {
      for (Nation nation : nations()) {
        if (nation.identity().equals(leader.identity())) {
          return nation;
        }
      }
    }
    return null;
  }

  @NonNull
  Collection<Player> players();

  @NonNull
  Collection<Nation> nations();

  @NonNull
  GameMap map();

  @NonNull
  @CheckReturnValue
  Action<Player> join(@NonNull Identity identity, @NonNull String name, @NonNull ColorId colorId);

  @NonNull
  @CheckReturnValue
  default Action<Player> join(@NonNull String name, @NonNull ColorId colorId) {
    return this.join(Identity.random(), name, colorId);
  }

  @CheckReturnValue
  default Action<Boolean> changeName(@NonNull Identity identity, @NonNull String newName) {
    for (Player p : players()) {
      if (p.identity().equals(identity)) {
        p.setName(newName);
        return new GenericAction<>(true);
      }
    }
    return new GenericAction<>(false);
  }

  @NonNull
  @CheckReturnValue
  Action<LeaveBundle> leave(@NonNull Identity identity);

  @NonNull
  @CheckReturnValue
  default Action<LeaveBundle> leave(@NonNull ColorId colorId) {
    var optPlayer = players().stream().filter(player -> player.colorId().equals(colorId)).findAny();
    return optPlayer.map(player -> leave(player.identity())).orElseGet(() -> new GenericAction<>(new IllegalStateException("no player with that color is present")));
  }

  @NonNull
  @CheckReturnValue
  Action<GameMap> selectMap(@NonNull RkmMap rkmMap, @NonNull MapOptions options);

  @NonNull
  @CheckReturnValue
  Action<Nation> selectTerritory(@NonNull Identity identity, @NonNull TerritoryId territoryId);

  @NonNull
  @CheckReturnValue
  Action<Player> start(@NonNull TurnOrder order);

  @NonNull
  @CheckReturnValue
  Action<SkipBundle> skip(@NonNull Identity identity);

  @NonNull
  @CheckReturnValue
  Action<SkipBundle> skipSelf(@NonNull Identity identity);

  @NonNull
  @CheckReturnValue
  Action<ClaimBundle> claim(@NonNull Identity identity, @NonNull TerritoryId... territoryIds);

  @NonNull
  @CheckReturnValue
  Action<UpdateBundle> update();

  @NonNull
  @CheckReturnValue
  CurrentStateBundle currentTurn();

}
