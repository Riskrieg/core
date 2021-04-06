package com.riskrieg.api.gamemode;

import com.riskrieg.api.gamemap.GameMap;
import com.riskrieg.api.player.Player;
import com.riskrieg.api.gamemap.territory.Territory;
import java.time.Instant;
import java.util.UUID;

// TODO: Add Javadoc
public interface Gamemode {

  UUID id();

  Instant creationTime();

  Instant lastUpdated();

  void join(Player player);

  void leave(Player player);

  void selectMap(GameMap map);

  void grant(Player player, Territory territory);

  void revoke(Player player, Territory territory);

}
