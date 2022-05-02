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

package com.riskrieg.core.old.internal;

import com.riskrieg.core.old.api.Group;
import com.riskrieg.core.old.api.Save;
import com.riskrieg.core.old.api.gamemode.GameMode;
import com.riskrieg.core.old.api.gamemode.brawl.BrawlMode;
import com.riskrieg.core.old.api.gamemode.classic.ClassicMode;
import com.riskrieg.core.old.api.gamemode.conquest.ConquestMode;
import com.riskrieg.core.old.api.gamemode.creative.CreativeMode;
import com.riskrieg.core.old.api.gamemode.regicide.RegicideMode;
import com.riskrieg.core.old.constant.Constants;
import com.riskrieg.core.old.internal.action.CompletableAction;
import com.riskrieg.core.old.internal.action.GenericAction;
import com.riskrieg.core.util.MoshiUtil;
import edu.umd.cs.findbugs.annotations.NonNull;
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
import java.util.stream.Stream;

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

  @NonNull
  @Override
  public String getId() {
    return path.getFileName().toString();
  }

  @NonNull
  @Override
  public <T extends GameMode> CompletableAction<T> createGame(@NonNull String gameId, @NonNull Class<T> type) {
    try {
      Path savePath = path.resolve(gameId + Constants.SAVE_FILE_EXT);
      if (Files.exists(savePath)) {
        var save = MoshiUtil.read(savePath, Save.class);
        var existingGame = type.getDeclaredConstructor(Save.class).newInstance(save);
        if (!existingGame.isEnded()) {
          throw new FileAlreadyExistsException("An active game already exists");
        }
      }
      var newGame = type.getDeclaredConstructor().newInstance();
      MoshiUtil.write(savePath, Save.class, new Save(newGame));
      return new GenericAction<>(newGame);
    } catch (Exception e) {
      return new GenericAction<>(e);
    }
  }

  @NonNull
  @Override
  public <T extends GameMode> CompletableAction<T> retrieveGameById(@NonNull String gameId, @NonNull Class<T> type) {
    try {
      Path savePath = path.resolve(gameId + Constants.SAVE_FILE_EXT);
      if (!Files.exists(savePath)) {
        throw new FileNotFoundException("Save file does not exist");
      }
      var save = MoshiUtil.read(savePath, Save.class);
      var game = type.getDeclaredConstructor(Save.class).newInstance(save);
      return new GenericAction<>(game);
    } catch (Exception e) {
      return new GenericAction<>(e);
    }
  }

  @NonNull
  @Override
  public CompletableAction<GameMode> retrieveGameById(@NonNull String gameId) {
    try {
      Path savePath = path.resolve(gameId + Constants.SAVE_FILE_EXT);
      if (!Files.exists(savePath)) {
        throw new FileNotFoundException("Save file does not exist");
      }
      var save = MoshiUtil.read(savePath, Save.class);
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

  @NonNull
  @Override
  public Set<GameMode> retrieveGames() {
    Set<GameMode> result = new HashSet<>();
    Set<Path> saves;

    try (Stream<Path> stream = Files.list(path)) {
      saves = stream.collect(Collectors.toSet());
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

  @NonNull
  @Override
  public <T extends GameMode> CompletableAction<T> saveGame(@NonNull String gameId, T game) {
    try {
      Path savePath = path.resolve(gameId + Constants.SAVE_FILE_EXT);
      MoshiUtil.write(savePath, Save.class, new Save(game));
      return new GenericAction<>(game);
    } catch (Exception e) {
      return new GenericAction<>(e);
    }
  }

  @Override
  public boolean deleteGame(String gameId) { // TODO: Delete by GameID instead of String
    try {
      boolean deleted = Files.deleteIfExists(path.resolve(gameId + Constants.SAVE_FILE_EXT));
      try (Stream<Path> stream = Files.list(path)) { // May or may not cause issues, locks files while counting.
        if (stream.findAny().isEmpty()) {
          Files.deleteIfExists(path);
        }
      }
      return deleted;
    } catch (IOException e) {
      return false;
    }
  }

}
