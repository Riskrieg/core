package com.riskrieg.core.api.game.mode;

import com.riskrieg.core.api.game.Game;
import com.riskrieg.core.api.game.GameMode;
import com.riskrieg.core.api.game.Save;
import com.riskrieg.core.internal.game.StandardGameMode;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.time.Instant;

public class Conquest implements Game {

  private final String id;

  private final Instant creationTime;
  private Instant updatedTime;

  public Conquest(String id) {
    this.id = id;
    this.creationTime = Instant.now();
    this.updatedTime = Instant.now();
  }

  public Conquest(Save save) {
    this.id = save.id();
    this.creationTime = save.creationTime();
    this.updatedTime = save.updatedTime();
  }

  @NonNull
  @Override
  public String id() {
    return id;
  }

  @NonNull
  @Override
  public GameMode mode() {
    return StandardGameMode.CONQUEST;
  }

  @NonNull
  @Override
  public Instant creationTime() {
    return creationTime;
  }

  @NonNull
  @Override
  public Instant updatedTime() {
    return updatedTime;
  }

}
