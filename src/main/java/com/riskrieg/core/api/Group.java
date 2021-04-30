package com.riskrieg.core.api;

import com.riskrieg.core.internal.action.CompletableAction;
import com.riskrieg.core.unsorted.gamemode.GameMode;
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

  // TODO: Add delete method

}
