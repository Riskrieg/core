package com.riskrieg.core.internal.group;

import com.riskrieg.core.api.game.Game;
import com.riskrieg.core.api.game.GameMode;
import com.riskrieg.core.api.group.Group;
import com.riskrieg.core.api.identifier.GameIdentifier;
import com.riskrieg.core.api.requests.GameAction;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;

public record LocalGroup(Path path) implements Group {

  public LocalGroup {
    Objects.requireNonNull(path);
    if (!Files.isDirectory(path)) {
      throw new IllegalStateException("The path provided must be a directory/folder.");
    }
  }

  @NonNull
  @Override
  public String id() {
    return path.getFileName().toString();
  }

  @NonNull
  @Override
  public GameAction<Game> createGame(GameMode mode, GameIdentifier identifier) {
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
    return null;
  }

}
