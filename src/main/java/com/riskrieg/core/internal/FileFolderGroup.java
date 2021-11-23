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

package com.riskrieg.core.internal;

import com.aaronjyoder.util.json.gson.GsonUtil;
import com.riskrieg.core.api.Group;
import com.riskrieg.core.api.Save;
import com.riskrieg.core.api.gamemode.GameID;
import com.riskrieg.core.api.gamemode.GameMode;
import com.riskrieg.core.api.gamemode.brawl.BrawlMode;
import com.riskrieg.core.api.gamemode.classic.ClassicMode;
import com.riskrieg.core.api.gamemode.conquest.ConquestMode;
import com.riskrieg.core.api.gamemode.creative.CreativeMode;
import com.riskrieg.core.api.gamemode.regicide.RegicideMode;
import com.riskrieg.core.constant.Constants;
import com.riskrieg.core.internal.action.CompletableAction;
import com.riskrieg.core.internal.action.GenericAction;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

public final class FileFolderGroup implements Group {

  private final Path path;

  public FileFolderGroup(Path path) {
    Objects.requireNonNull(path);
    this.path = path;
  }

  public Path getPath() {
    return path;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FileFolderGroup group = (FileFolderGroup) o;
    return path.equals(group.path);
  }

  @Override
  public int hashCode() {
    return Objects.hash(path);
  }

  @Nonnull
  @Override
  public String getId() {
    return path.getFileName().toString();
  }

  @Nonnull
  @Override
  public <T extends GameMode> CompletableAction<T> createGame(@Nonnull String gameId, @Nonnull Class<T> type) {
    try {
      Path savePath = path.resolve(gameId + Constants.SAVE_FILE_EXT);
      if (Files.exists(savePath)) {
        var save = GsonUtil.read(savePath, Save.class);
        var existingGame = type.getDeclaredConstructor(Save.class).newInstance(save);
        if (!existingGame.isEnded()) {
          throw new FileAlreadyExistsException("An active game already exists");
        }
      }
      var newGame = type.getDeclaredConstructor().newInstance();
      GsonUtil.write(savePath, Save.class, new Save(newGame));
      return new GenericAction<>(newGame);
    } catch (Exception e) {
      return new GenericAction<>(e);
    }
  }

  @Nonnull
  @Override
  public <T extends GameMode> CompletableAction<T> retrieveGameById(@Nonnull String gameId, @Nonnull Class<T> type) {
    try {
      Path savePath = path.resolve(gameId + Constants.SAVE_FILE_EXT);
      if (!Files.exists(savePath)) {
        throw new FileNotFoundException("Save file does not exist");
      }
      var save = GsonUtil.read(savePath, Save.class);
      var game = type.getDeclaredConstructor(Save.class).newInstance(save);
      return new GenericAction<>(game);
    } catch (Exception e) {
      return new GenericAction<>(e);
    }
  }

  @Nonnull
  @Override
  public CompletableAction<GameMode> retrieveGameById(@Nonnull String gameId) {
    try {
      Path savePath = path.resolve(gameId + Constants.SAVE_FILE_EXT);
      if (!Files.exists(savePath)) {
        throw new FileNotFoundException("Save file does not exist");
      }
      var save = GsonUtil.read(savePath, Save.class);
      if (save == null) {
        throw new IllegalStateException("Unable to read save file");
      }
      return switch (save.gameType()) {
        case CLASSIC -> new GenericAction<>(new ClassicMode(save));
        case CONQUEST -> new GenericAction<>(new ConquestMode(save));
        case REGICIDE -> new GenericAction<>(new RegicideMode(save));
        case BRAWL -> new GenericAction<>(new BrawlMode(save));
        case CREATIVE -> new GenericAction<>(new CreativeMode(save));
        default -> throw new IllegalStateException("Invalid game mode");
      };
    } catch (Exception e) {
      return new GenericAction<>(e);
    }
  }

  @Nonnull
  @Override
  public Set<GameMode> retrieveGames() {
    Set<GameMode> result = new HashSet<>();
    Set<Path> saves;
    try {
      saves = Files.list(path).collect(Collectors.toSet());
    } catch (IOException e) {
      saves = new HashSet<>();
    }
    for (Path path : saves) {
      if (path.getFileName().toString().endsWith(Constants.SAVE_FILE_EXT)) {
        var fileName = path.getFileName().toString().split(Constants.SAVE_FILE_EXT)[0].trim();
        retrieveGameById(fileName).complete().ifPresent(result::add);
      }
    }
    return Collections.unmodifiableSet(result);
  }

  @Nonnull
  @Override
  public <T extends GameMode> CompletableAction<T> saveGame(@Nonnull String gameId, T game) {
    try {
      Path savePath = path.resolve(gameId + Constants.SAVE_FILE_EXT);
      GsonUtil.write(savePath, Save.class, new Save(game));
      return new GenericAction<>(game);
    } catch (Exception e) {
      return new GenericAction<>(e);
    }
  }

  @Override
  public boolean deleteGame(String gameId) { // TODO: Delete by GameID instead of String
    try {
      boolean deleted = Files.deleteIfExists(path.resolve(gameId + Constants.SAVE_FILE_EXT));
      if (Files.list(path).findAny().isEmpty()) { // May or may not cause issues, locks files while counting.
        Files.deleteIfExists(path);
      }
      return deleted;
    } catch (IOException e) {
      return false;
    }
  }

}
