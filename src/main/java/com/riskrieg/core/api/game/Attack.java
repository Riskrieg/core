package com.riskrieg.core.api.game;

import com.riskrieg.core.api.game.territory.GameTerritory;
import com.riskrieg.core.api.identifier.NationIdentifier;

@FunctionalInterface
public interface Attack {

  /**
   * @param attacker  The nation doing the attack
   * @param defender  The nation who owns the given territory, defending from the attacker
   * @param territory The territory owned by the defender
   * @return {@code true} if the attacker wins the engagement, otherwise {@code false}
   */
  boolean success(NationIdentifier attacker, NationIdentifier defender, GameTerritory territory);

}
