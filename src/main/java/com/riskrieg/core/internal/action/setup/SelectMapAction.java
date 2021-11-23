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
import com.riskrieg.core.api.map.MapOptions;
import com.riskrieg.core.api.map.options.Availability;
import com.riskrieg.core.api.nation.Nation;
import com.riskrieg.core.internal.action.Action;
import com.riskrieg.map.RkmMap;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import javax.annotation.Nullable;

public final class SelectMapAction implements Action<GameMap> {

  private final RkmMap rkmMap;
  private final MapOptions options;
  private final GameState gameState;
  private final GameMap gameMap;
  private final Collection<Nation> nations;

  public SelectMapAction(RkmMap rkmMap, MapOptions options, GameState gameState, GameMap gameMap, Collection<Nation> nations) {
    this.rkmMap = rkmMap;
    this.options = options;
    this.gameState = gameState;
    this.gameMap = gameMap;
    this.nations = nations;
  }

  @Override
  public void submit(@Nullable Consumer<? super GameMap> success, @Nullable Consumer<? super Throwable> failure) {
    try {
      switch (gameState) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case SETUP -> {
          Objects.requireNonNull(rkmMap);
          Objects.requireNonNull(options);
          if (!options.availability().equals(Availability.AVAILABLE)) {
            throw new IllegalStateException("That map is not available");
          }
          gameMap.set(rkmMap, options);
          nations.clear();
          if (success != null) {
            success.accept(gameMap);
          }
        }
        default -> throw new IllegalStateException("The map can only be set during the setup phase");
      }
    } catch (Exception e) {
      if (failure != null) {
        failure.accept(e);
      }
    }
  }

}
