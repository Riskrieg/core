package com.riskrieg.api.action;

import java.util.function.Consumer;
import javax.annotation.Nullable;

public class StartAction implements GameAction<Void> {

  public StartAction() {

  }

  @Override
  public void submit(@Nullable Consumer<? super Void> success, @Nullable Consumer<? super Throwable> failure) {
  }

}
