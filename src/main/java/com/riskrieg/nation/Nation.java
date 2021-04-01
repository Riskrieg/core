package com.riskrieg.nation;

import com.riskrieg.map.GameMap;
import com.riskrieg.map.graph.Territory;
import com.riskrieg.player.PlayerIdentifier;
import java.util.Set;

public interface Nation {

  PlayerIdentifier getLeaderIdentifier();

  Territory getCapital();

  Set<Territory> getTerritories();

  Set<Territory> getNeighbors(GameMap map);

  boolean addTerritory(Territory territory);

  boolean removeTerritory(Territory territory);

}
