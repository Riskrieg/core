package com.riskrieg.api.gamemode;

import com.riskrieg.api.gamemode.order.TurnOrder;
import com.riskrieg.api.player.Player;
import com.riskrieg.map.GameMap;
import com.riskrieg.map.territory.Territory;
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

  void start(TurnOrder order);

}
