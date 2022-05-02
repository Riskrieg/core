package com.riskrieg.core.internal.game.player;

import com.riskrieg.core.api.game.player.Player;
import edu.umd.cs.findbugs.annotations.NonNull;

public record StandardPlayer(String id, String name) implements Player {

  @NonNull
  @Override
  public String id() {
    return id;
  }

  @NonNull
  @Override
  public String name() {
    return name;
  }

}
