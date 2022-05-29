package com.riskrieg.core.util.game;

import com.riskrieg.core.api.game.map.GameMap;
import com.riskrieg.core.api.game.territory.Claim;
import com.riskrieg.core.api.game.territory.TerritoryType;
import com.riskrieg.core.api.identifier.TerritoryIdentifier;
import java.util.Set;

public class GameUtil {

  private GameUtil() {
  }

  /* Territory-related utilities */

  /**
   * Determines whether the provided territory exists on the provided map.
   *
   * @param identifier The identifier of the territory
   * @param map        The map the territory should be searched for on
   * @return {@code true} if the territory can be found, otherwise {@code false}
   */
  public static boolean territoryExists(TerritoryIdentifier identifier, GameMap map) {
    return map.vertices().stream().anyMatch(territory -> territory.identifier().equals(identifier));
  }

  /**
   * Determines whether the provided territory does not exist on the provided map.
   *
   * @param identifier The identifier of the territory
   * @param map        The map the territory should be searched for on
   * @return {@code true} if the territory cannot be found, otherwise {@code false}
   */
  public static boolean territoryNotExists(TerritoryIdentifier identifier, GameMap map) {
    return map.vertices().stream().noneMatch(territory -> territory.identifier().equals(identifier));
  }

  /**
   * Determines whether the provided territory has been claimed by any nation.
   *
   * @param identifier The identifier of the territory
   * @param allClaims  The set of claims to search through
   * @return {@code true} if the territory is claimed by a nation, otherwise {@code false}
   */
  public static boolean territoryIsClaimed(TerritoryIdentifier identifier, Set<Claim> allClaims) {
    return allClaims.stream().anyMatch(claim -> claim.territory().identifier().equals(identifier));
  }

  /**
   * Determines whether a territory is of the provided type.
   *
   * @param identifier The identifier of the territory
   * @param type       The type to check the provided territory against
   * @param allClaims  The set of claims to search through
   * @return {@code true} if the territory is of the provided type, otherwise {@code false}
   */
  public static boolean territoryIsOfType(TerritoryIdentifier identifier, TerritoryType type, Set<Claim> allClaims) {
    var claim = allClaims.stream().filter(c -> c.territory().identifier().equals(identifier)).findFirst();
    return claim.isPresent() && claim.get().territory().type().equals(type);
  }

}
