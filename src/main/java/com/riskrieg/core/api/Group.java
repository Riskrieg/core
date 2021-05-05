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

  // TODO: Add delete method

}
