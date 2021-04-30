package com.riskrieg.core.api;

import com.riskrieg.core.internal.action.Action;
import com.riskrieg.core.unsorted.gamemode.GameMode;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public interface Group {

  @Nonnull
  String getId();

  @Nonnull
  @CheckReturnValue
  <T extends GameMode> Action<T> createGame(@Nonnull String gameId, @Nonnull Class<T> type);

  @Nonnull
  @CheckReturnValue
  <T extends GameMode> Action<T> retrieveGameById(@Nonnull String gameId, @Nonnull Class<T> type);

  @Nonnull
  @CheckReturnValue
  Action<GameMode> retrieveGameById(@Nonnull String gameId);

  // TODO: Add delete method

}
