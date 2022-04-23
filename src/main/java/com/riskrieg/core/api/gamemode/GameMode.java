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

package com.riskrieg.core.api.gamemode;

import com.riskrieg.core.api.map.GameMap;
import com.riskrieg.core.api.map.MapOptions;
import com.riskrieg.core.api.nation.Nation;
import com.riskrieg.core.api.order.TurnOrder;
import com.riskrieg.core.api.player.Identity;
import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.constant.color.ColorId;
import com.riskrieg.core.constant.color.PlayerColor;
import com.riskrieg.core.internal.action.Action;
import com.riskrieg.core.internal.action.GenericAction;
import com.riskrieg.core.internal.bundle.ClaimBundle;
import com.riskrieg.core.internal.bundle.CurrentStateBundle;
import com.riskrieg.core.internal.bundle.LeaveBundle;
import com.riskrieg.core.internal.bundle.SkipBundle;
import com.riskrieg.core.internal.bundle.UpdateBundle;
import com.riskrieg.map.RkmMap;
import com.riskrieg.map.territory.TerritoryId;
import java.time.Instant;
import java.util.Collection;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface GameMode {

  @Nonnull
  String displayName();

  @Nonnull
  GameID id();

  @Nonnull
  Instant creationTime();

  @Nonnull
  Instant lastUpdated();

  @Nonnull
  GameState gameState();

  void setGameState(@Nonnull GameState gameState);

  boolean isEnded();

  @Nullable
  default Player getPlayer(@Nonnull Identity identity) {
    for (Player player : players()) {
      if (player.identity().equals(identity)) {
        return player;
      }
    }
    return null;
  }

  @Nullable
  default Player getPlayer(@Nonnull PlayerColor playerColor) {
    for (Player player : players()) {
      if (player.colorId().equals(playerColor.id())) {
        return player;
      }
    }
    return null;
  }

  @Nullable
  default Nation getNation(@Nonnull Identity identity) {
    for (Nation nation : nations()) {
      if (nation.identity().equals(identity)) {
        return nation;
      }
    }
    return null;
  }

  @Nullable
  default Nation getNation(@Nonnull PlayerColor playerColor) {
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

  @Nonnull
  Collection<Player> players();

  @Nonnull
  Collection<Nation> nations();

  @Nonnull
  GameMap map();

  @Nonnull
  @CheckReturnValue
  Action<Player> join(@Nonnull Identity identity, @Nonnull String name, @Nonnull ColorId colorId);

  @Nonnull
  @CheckReturnValue
  default Action<Player> join(@Nonnull String name, @Nonnull ColorId colorId) {
    return this.join(Identity.random(), name, colorId);
  }

  @CheckReturnValue
  default Action<Boolean> changeName(@Nonnull Identity identity, String newName) {
    for (Player p : players()) {
      if (p.identity().equals(identity)) {
        p.setName(newName);
        return new GenericAction<>(true);
      }
    }
    return new GenericAction<>(false);
  }

  @Nonnull
  @CheckReturnValue
  Action<LeaveBundle> leave(@Nonnull Identity identity);

  @Nonnull
  @CheckReturnValue
  default Action<LeaveBundle> leave(@Nonnull ColorId colorId) {
    var optPlayer = players().stream().filter(player -> player.colorId().equals(colorId)).findAny();
    return optPlayer.map(player -> leave(player.identity())).orElseGet(() -> new GenericAction<>(new IllegalStateException("no player with that color is present")));
  }

  @Nonnull
  @CheckReturnValue
  Action<GameMap> selectMap(@Nonnull RkmMap rkmMap, @Nonnull MapOptions options);

  @Nonnull
  @CheckReturnValue
  Action<Nation> selectTerritory(@Nonnull Identity identity, @Nonnull TerritoryId territoryId);

  @Nonnull
  @CheckReturnValue
  Action<Player> start(@Nonnull TurnOrder order);

  @Nonnull
  @CheckReturnValue
  Action<SkipBundle> skip(Identity identity);

  @Nonnull
  @CheckReturnValue
  Action<SkipBundle> skipSelf(Identity identity);

  @Nonnull
  @CheckReturnValue
  Action<ClaimBundle> claim(Identity identity, TerritoryId... territoryIds);

  @Nonnull
  @CheckReturnValue
  Action<UpdateBundle> update();

  @Nonnull
  @CheckReturnValue
  CurrentStateBundle currentTurn();

}
