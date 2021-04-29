package com.riskrieg.core.internal.impl;

import com.aaronjyoder.util.json.gson.GsonUtil;
import com.riskrieg.core.api.Group;
import com.riskrieg.core.api.gamemode.Save;
import com.riskrieg.core.internal.action.Action;
import com.riskrieg.core.internal.action.GenericAction;
import com.riskrieg.core.unsorted.constant.Constants;
import com.riskrieg.core.unsorted.gamemode.GameMode;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
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
  public <T extends GameMode> Action<T> createGame(@Nonnull String gameId, @Nonnull Class<T> type) {
    try {
      Path savePath = path.resolve(gameId + Constants.SAVE_FILE_EXT);
      if (Files.exists(savePath)) {
        var save = GsonUtil.read(savePath, Save.class);
        var existingGame = type.getDeclaredConstructor(GroupImpl.class, Save.class).newInstance(this, save);
        if (!existingGame.isEnded()) {
          throw new FileAlreadyExistsException("An active game already exists");
        }
      }
      var newGame = type.getDeclaredConstructor(GroupImpl.class).newInstance(this);
      GsonUtil.write(savePath, Save.class, new Save(newGame));
      return new GenericAction<>(newGame);
    } catch (Exception e) {
      return new GenericAction<>(e);
    }
  }

  @Nonnull
  @Override
  public <T extends GameMode> Action<T> retrieveGameById(@Nonnull String gameId, @Nonnull Class<T> type) {
    try {
      Path savePath = path.resolve(gameId + Constants.SAVE_FILE_EXT);
      if (!Files.exists(savePath)) {
        throw new FileNotFoundException("Save file does not exist");
      }
      var save = GsonUtil.read(savePath, Save.class);
      var game = type.getDeclaredConstructor(GroupImpl.class, Save.class).newInstance(this, save);
      return new GenericAction<>(game);
    } catch (Exception e) {
      return new GenericAction<>(e);
    }
  }

}
