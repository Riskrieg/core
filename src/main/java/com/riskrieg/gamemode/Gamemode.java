package com.riskrieg.gamemode;

import com.riskrieg.gamemode.order.TurnOrder;
import com.riskrieg.gamemode.util.GameID;
import com.riskrieg.gamemode.util.Moment;
import com.riskrieg.nation.Nation;
import com.riskrieg.player.Player;
import com.riskrieg.map.GameMap;
import com.riskrieg.map.territory.TerritoryId;
import java.util.Collection;

// TODO: Add Javadoc
public interface Gamemode {

  GameID id();

  Moment creationTime();

  Moment lastUpdated();

  Collection<Player> players();

  Collection<Nation> nations();

  GameMap map();

  void join(Player player);

  void leave(Player player);

  void selectMap(GameMap map);

  void setCapital(Player player, TerritoryId id);

  void grant(Player player, TerritoryId id);

  void revoke(Player player, TerritoryId id);

  void start(TurnOrder order);

}
