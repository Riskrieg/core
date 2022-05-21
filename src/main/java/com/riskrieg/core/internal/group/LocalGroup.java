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

package com.riskrieg.core.internal.group;

import com.riskrieg.core.api.color.ColorBatch;
import com.riskrieg.core.api.game.Game;
import com.riskrieg.core.api.game.GameConstants;
import com.riskrieg.core.api.game.GameMode;
import com.riskrieg.core.api.game.Save;
import com.riskrieg.core.api.group.Group;
import com.riskrieg.core.api.identifier.GameIdentifier;
import com.riskrieg.core.api.identifier.GroupIdentifier;
import com.riskrieg.core.api.requests.GameAction;
import com.riskrieg.core.internal.requests.GenericAction;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;

public record LocalGroup(Path path) implements Group { // TODO: Implement saves in order to finish this class

  public LocalGroup {
    Objects.requireNonNull(path);
    if (!Files.isDirectory(path)) {
      throw new IllegalStateException("The path provided must be a directory/folder.");
    }
  }

  @Override
  public GroupIdentifier identifier() {
    return GroupIdentifier.of(path.getName(path.getNameCount() - 1).toString());
  }

  @NonNull
  @Override
  public GameAction<Game> createGame(GameMode mode, GameConstants constants, ColorBatch batch, GameIdentifier identifier) {
    Path savePath = path.resolve(identifier.id() + Save.FILE_EXT);
    return null;
  }

  @NonNull
  @Override
  public GameAction<Game> retrieveGame(GameIdentifier identifier) {
    return null;
  }

  @NonNull
  @Override
  public GameAction<Collection<Game>> retrieveAllGames() {
    return null;
  }

  @NonNull
  @Override
  public GameAction<Boolean> saveGame(Game game) {
    return null;
  }

  @NonNull
  @Override
  public GameAction<Boolean> deleteGame(GameIdentifier identifier) {
    Path savePath = path.resolve(identifier.id() + Save.FILE_EXT);
    try {
      return new GenericAction<>(Files.deleteIfExists(savePath));
    } catch (IOException e) {
      return new GenericAction<>(false, e);
    }
  }

}
