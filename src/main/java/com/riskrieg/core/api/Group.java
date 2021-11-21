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

package com.riskrieg.core.api;

import com.riskrieg.core.api.gamemode.GameMode;
import com.riskrieg.core.internal.action.Action;
import com.riskrieg.core.internal.action.CompletableAction;
import java.util.Set;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public interface Group {

  @Nonnull
  String getId();

  @Nonnull
  @CheckReturnValue
  <T extends GameMode> CompletableAction<T> createGame(@Nonnull String gameId, @Nonnull Class<T> type);

  @Nonnull
  @CheckReturnValue
  <T extends GameMode> CompletableAction<T> retrieveGameById(@Nonnull String gameId, @Nonnull Class<T> type);

  @Nonnull
  @CheckReturnValue
  CompletableAction<GameMode> retrieveGameById(@Nonnull String gameId);

  @Nonnull
  Set<GameMode> retrieveGames();

  @Nonnull
  @CheckReturnValue
  <T extends GameMode> Action<T> saveGame(@Nonnull String gameId, T game);

  boolean deleteGame(String gameId);

}
