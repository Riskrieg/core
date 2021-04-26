package com.riskrieg.core.internal.action.api;

import com.riskrieg.core.gamemode.GameMode;
import java.util.function.Consumer;
import javax.annotation.Nullable;

public class CreateAction<T extends GameMode> implements ApiAction<T> {

  private final T game;
  private final Throwable throwable;

  public CreateAction(Throwable throwable) {
    this.game = null;
    this.throwable = throwable;
  }

  public CreateAction(T game) {
    this.game = game;
    this.throwable = null;
  }

  @Override
  public void complete(@Nullable Consumer<? super T> success, @Nullable Consumer<? super Throwable> failure) {
    if (success != null && game != null) {
      success.accept(game);
    }
    if (failure != null && throwable != null) {
      failure.accept(throwable);
    }
  }

}
