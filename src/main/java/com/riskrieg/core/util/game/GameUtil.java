package com.riskrieg.core.util.game;

import com.riskrieg.core.api.game.map.GameMap;
import com.riskrieg.core.api.game.territory.Claim;
import com.riskrieg.core.api.game.territory.TerritoryType;
import com.riskrieg.core.api.identifier.NationIdentifier;
import com.riskrieg.core.api.identifier.TerritoryIdentifier;
import java.util.Set;

public class GameUtil {

  private GameUtil() {
  }

  public static boolean territoryExists(TerritoryIdentifier identifier, GameMap map) {
    return map.vertices().stream().anyMatch(territory -> territory.identifier().equals(identifier));
  }

  public static boolean territoryNotExists(TerritoryIdentifier identifier, GameMap map) {
    return map.vertices().stream().noneMatch(territory -> territory.identifier().equals(identifier));
  }

  public static boolean territoryIsClaimed(TerritoryIdentifier identifier, Set<Claim> allClaims) {
    return allClaims.stream().anyMatch(claim -> claim.territory().identifier().equals(identifier));
  }

  public static boolean nationHasAnyClaim(NationIdentifier identifier, Set<Claim> allClaims) {
    return allClaims.stream().anyMatch(claim -> claim.identifier().equals(identifier));
  }

  public static boolean nationHasAnyClaim(NationIdentifier identifier, Set<Claim> allClaims, TerritoryType type) {
    return allClaims.stream().anyMatch(claim -> claim.identifier().equals(identifier) && claim.territory().type().equals(type));
  }

  public static long getClaimCount(NationIdentifier identifier, Set<Claim> allClaims) {
    return allClaims.stream().filter(claim -> claim.identifier().equals(identifier)).count();
  }

}
