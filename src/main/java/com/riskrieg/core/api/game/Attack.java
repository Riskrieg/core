package com.riskrieg.core.api.game;

import com.riskrieg.core.api.game.entity.nation.Nation;
import com.riskrieg.core.api.game.map.GameMap;
import com.riskrieg.core.api.game.territory.Claim;
import com.riskrieg.core.api.identifier.TerritoryIdentifier;
import java.util.Set;

@FunctionalInterface
public interface Attack {

  /**
   * @param attacker            The nation doing the attack
   * @param defender            The nation who owns the given territory, defending from the attacker
   * @param territoryIdentifier The identifier of the territory owned by the defender
   * @param map                 The map the attack takes place on
   * @param claims              The immutable set of all claims made by all nations
   * @param constants           The set of game constants to use for attack purposes
   * @return {@code true} if the attacker wins the engagement, otherwise {@code false}
   */
  boolean success(Nation attacker, Nation defender, TerritoryIdentifier territoryIdentifier, GameMap map, Set<Claim> claims, GameConstants constants);

}
