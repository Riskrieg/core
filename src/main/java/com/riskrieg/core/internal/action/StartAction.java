package com.riskrieg.core.internal.action;

import java.util.function.Consumer;
import javax.annotation.Nullable;

public final class StartAction implements GameAction<Void> {

  public StartAction() {

  }

  @Override
  public void submit(@Nullable Consumer<? super Void> success, @Nullable Consumer<? super Throwable> failure) {
  }

}
