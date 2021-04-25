package com.riskrieg.core.internal.action;

import com.riskrieg.core.gamemode.GameState;
import com.riskrieg.core.map.GameMap;
import com.riskrieg.core.map.MapOptions;
import com.riskrieg.core.map.options.Availability;
import com.riskrieg.core.nation.Nation;
import com.riskrieg.map.RkmMap;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import javax.annotation.Nullable;

public final class SelectMapAction implements GameAction<GameMap> {

  private final RkmMap rkmMap;
  private final MapOptions options;
  private final GameState gameState;
  private final GameMap gameMap;
  private final Collection<Nation> nations;

  public SelectMapAction(RkmMap rkmMap, MapOptions options, GameState gameState, GameMap gameMap, Collection<Nation> nations) {
    this.rkmMap = rkmMap;
    this.options = options;
    this.gameState = gameState;
    this.gameMap = gameMap;
    this.nations = nations;
  }

  @Override
  public void submit(@Nullable Consumer<? super GameMap> success, @Nullable Consumer<? super Throwable> failure) {
    try {
      switch (gameState) {
        case ENDED, RUNNING -> throw new IllegalStateException("The map can only be set during the setup phase");
        case SETUP -> {
          Objects.requireNonNull(rkmMap);
          Objects.requireNonNull(options);
          if (!options.availability().equals(Availability.AVAILABLE)) {
            throw new IllegalStateException("That map is not available");
          }
          gameMap.set(rkmMap, options);
          nations.clear();
          if (success != null) {
            success.accept(gameMap);
          }
        }
      }
    } catch (Exception e) {
      if (failure != null) {
        failure.accept(e);
      }
    }
  }

}
