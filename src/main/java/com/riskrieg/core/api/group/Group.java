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

package com.riskrieg.core.api.group;

import com.riskrieg.core.api.color.ColorBatch;
import com.riskrieg.core.api.game.Game;
import com.riskrieg.core.api.game.GameConstants;
import com.riskrieg.core.api.game.GameMode;
import com.riskrieg.core.api.identifier.GameIdentifier;
import com.riskrieg.core.api.identifier.GroupIdentifier;
import com.riskrieg.core.api.requests.GameAction;
import edu.umd.cs.findbugs.annotations.CheckReturnValue;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.Collection;

/**
 * A {@link Group} is conceptually similar to a folder. {@link Group}s contain collections of {@link Game} objects.
 */
public interface Group {

  GroupIdentifier identifier();

  @NonNull
  @CheckReturnValue
  default GameAction<Game> createGame(GameMode mode, GameConstants constants, ColorBatch batch) {
    return createGame(mode, constants, batch, GameIdentifier.uuid());
  }

  @NonNull
  @CheckReturnValue
  GameAction<Game> createGame(GameMode mode, GameConstants constants, ColorBatch batch, GameIdentifier identifier);

  @NonNull
  @CheckReturnValue
  GameAction<Game> retrieveGame(GameIdentifier identifier);

  @NonNull
  @CheckReturnValue
  GameAction<Collection<Game>> retrieveAllGames();

  @NonNull
  @CheckReturnValue
  GameAction<Boolean> saveGame(Game game);

  @NonNull
  @CheckReturnValue
  GameAction<Boolean> deleteGame(GameIdentifier identifier);

}
