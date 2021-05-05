package com.riskrieg.core.internal.impl;

import com.aaronjyoder.util.json.gson.GsonUtil;
import com.riskrieg.core.api.Group;
import com.riskrieg.core.api.Save;
import com.riskrieg.core.api.gamemode.GameMode;
import com.riskrieg.core.api.gamemode.brawl.BrawlMode;
import com.riskrieg.core.api.gamemode.classic.ClassicMode;
import com.riskrieg.core.api.gamemode.conquest.ConquestMode;
import com.riskrieg.core.api.gamemode.creative.CreativeMode;
import com.riskrieg.core.api.gamemode.siege.SiegeMode;
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

public final class GroupImpl implements Group {

  private final Path path;

  public GroupImpl(Path path) {
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
    GroupImpl group = (GroupImpl) o;
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
      return switch (save.getGameType()) {
        case CLASSIC -> new GenericAction<>(new ClassicMode(save));
        case CONQUEST -> new GenericAction<>(new ConquestMode(save));
        case SIEGE -> new GenericAction<>(new SiegeMode(save));
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

}
