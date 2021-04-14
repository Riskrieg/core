package com.riskrieg.api.gamemode;

import com.riskrieg.api.gamemode.order.TurnOrder;
import com.riskrieg.api.nation.Nation;
import com.riskrieg.api.player.Player;
import com.riskrieg.map.GameMap;
import com.riskrieg.map.territory.Territory;
import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

// TODO: Add Javadoc
public interface Gamemode {

  UUID id();

  Instant creationTime();

  Instant lastUpdated();

  Collection<Player> players();

  Collection<Nation> nations();

  Collection<Territory> capitals();

  GameMap map();

  void join(Player player);

  void leave(Player player);

  void selectMap(GameMap map);

  void setCapital(Player player, Territory territory);

  void grant(Player player, Territory territory);

  void revoke(Player player, Territory territory);

  void start(TurnOrder order);

}
