package com.riskrieg.api.action;

import com.riskrieg.constant.Constants;
import com.riskrieg.gamemode.GameState;
import com.riskrieg.map.GameMap;
import com.riskrieg.map.MapOptions;
import com.riskrieg.map.RkmMap;
import com.riskrieg.nation.Nation;
import java.io.File;
import java.util.Collection;
import java.util.function.Consumer;
import javax.annotation.Nullable;

public class SelectMapAction implements GameAction<GameMap> {

  private final String mapName;
  private final GameMap gameMap;
  private final Collection<Nation> nations;
  private final GameState gameState;

  public SelectMapAction(String mapName, GameMap gameMap, Collection<Nation> nations, GameState gameState) {
    this.mapName = mapName;
    this.gameMap = gameMap;
    this.nations = nations;
    this.gameState = gameState;
  }

  @Override
  public void submit(@Nullable Consumer<? super GameMap> success, @Nullable Consumer<? super Throwable> failure) {
    try {
      switch (gameState) {
        case ENDED, RUNNING -> throw new IllegalStateException("The map can only be set during the setup phase");
        case SETUP -> {
          var rkm = RkmMap.load(new File(Constants.MAP_PATH + mapName + ".rkm").toURI()).orElseThrow();
          var options = MapOptions.load(rkm.mapName(), false);
          gameMap.set(rkm, options);
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
