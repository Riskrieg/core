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

import com.riskrieg.core.api.game.Game;
import com.riskrieg.core.api.game.GameConstants;
import com.riskrieg.core.api.game.GamePhase;
import com.riskrieg.core.api.game.Save;
import com.riskrieg.core.api.game.feature.FeatureFlag;
import com.riskrieg.core.api.group.Group;
import com.riskrieg.core.api.identifier.GameIdentifier;
import com.riskrieg.core.api.identifier.GroupIdentifier;
import com.riskrieg.core.api.requests.GameAction;
import com.riskrieg.core.internal.legacy.LegacySave;
import com.riskrieg.core.internal.requests.GenericAction;
import com.riskrieg.core.util.io.RkJsonUtil;
import com.riskrieg.palette.RkpPalette;
import edu.umd.cs.findbugs.annotations.CheckReturnValue;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record LocalGroup(Path path) implements Group {

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
  @CheckReturnValue
  public <T extends Game> GameAction<Game> createGame(GameConstants constants, RkpPalette palette, GameIdentifier identifier, Class<T> type, FeatureFlag... featureFlags) {
    Objects.requireNonNull(constants);
    Objects.requireNonNull(palette);
    Objects.requireNonNull(identifier);
    Objects.requireNonNull(type);
    Objects.requireNonNull(featureFlags);
    Path savePath = path.resolve(identifier.id() + Save.FILE_EXT);
    try {
      if (Files.exists(savePath)) {

        Save save = null;
        try {
          save = RkJsonUtil.read(savePath, Save.class);
        } catch (Exception e) {
          LegacySave legacySave = RkJsonUtil.read(savePath, LegacySave.class);
          if (legacySave != null) {
            save = legacySave.toSave(identifier);
          }
        }

        if (save == null) {
          throw new IllegalStateException("Save is null");
        }
        Path repositoryRoot = savePath.getParent().getParent().getParent();
        Path mapRepository = repositoryRoot.resolve("maps/");
        Game game = save.type().getDeclaredConstructor(Save.class, Path.class).newInstance(save, mapRepository);
        if (!game.phase().equals(GamePhase.ENDED)) {
          throw new FileAlreadyExistsException("An active game already exists");
        }
      }
      var newGame = type.getDeclaredConstructor(GameIdentifier.class, GameConstants.class, RkpPalette.class, FeatureFlag[].class)
          .newInstance(identifier, constants, palette, featureFlags);
      RkJsonUtil.write(savePath, Save.class, new Save(newGame, type));
      return new GenericAction<>(newGame);
    } catch (Exception e) {
      return new GenericAction<>(e);
    }
  }

  @NonNull
  @Override
  @CheckReturnValue
  public GameAction<Game> retrieveGame(GameIdentifier identifier) {
    Objects.requireNonNull(identifier);
    Path savePath = path.resolve(identifier.id() + Save.FILE_EXT);
    try {
      if (Files.notExists(savePath)) {
        throw new FileNotFoundException("Save file does not exist");
      }

      Save save = null;
      try {
        save = RkJsonUtil.read(savePath, Save.class);
      } catch (Exception e) {
        LegacySave legacySave = RkJsonUtil.read(savePath, LegacySave.class);
        if (legacySave != null) {
          save = legacySave.toSave(identifier);
        }
      }

      if (save == null) {
        throw new IllegalStateException("Save is null");
      }
      Path repositoryRoot = savePath.getParent().getParent().getParent();
      Path mapRepository = repositoryRoot.resolve("maps/");
      Game game = save.type().getDeclaredConstructor(Save.class, Path.class).newInstance(save, mapRepository);
      return new GenericAction<>(game);
    } catch (Exception e) {
      return new GenericAction<>(e);
    }
  }

  @NonNull
  @Override
  @CheckReturnValue
  public GameAction<Collection<Game>> retrieveAllGames() {
    List<Game> result = new ArrayList<>();
    Set<Path> savePaths;
    try (Stream<Path> stream = Files.list(path)) {
      savePaths = stream.collect(Collectors.toSet());
    } catch (Exception e) {
      savePaths = new HashSet<>();
    }
    savePaths.stream()
        .filter(p -> p.getFileName().toString().endsWith(Save.FILE_EXT))
        .forEach(savePath -> {
          String fileName = savePath.getFileName().toString().split(Save.FILE_EXT)[0].trim();
          Game game = retrieveGame(GameIdentifier.of(fileName)).complete();
          result.add(game);
        });
    return new GenericAction<>(Collections.unmodifiableCollection(result));
  }

  @NonNull
  @Override
  @CheckReturnValue
  public GameAction<Boolean> saveGame(Game game) {
    Objects.requireNonNull(game);
    Path savePath = path.resolve(game.identifier().id() + Save.FILE_EXT);
    try {
      RkJsonUtil.write(savePath, Save.class, new Save(game, game.getClass()));
      return new GenericAction<>(true);
    } catch (Exception e) {
      return new GenericAction<>(false, e);
    }
  }

  @NonNull
  @Override
  @CheckReturnValue
  public GameAction<Boolean> deleteGame(GameIdentifier identifier) {
    Objects.requireNonNull(identifier);
    Path savePath = path.resolve(identifier.id() + Save.FILE_EXT);
    try {
      return new GenericAction<>(Files.deleteIfExists(savePath));
    } catch (IOException e) {
      return new GenericAction<>(false, e);
    }
  }

}
