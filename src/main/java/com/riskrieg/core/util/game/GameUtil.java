package com.riskrieg.core.util.game;

import com.riskrieg.core.api.game.GameConstants;
import com.riskrieg.core.api.game.map.GameMap;
import com.riskrieg.core.api.game.territory.Claim;
import com.riskrieg.core.api.game.territory.GameTerritory;
import com.riskrieg.core.api.game.territory.TerritoryType;
import com.riskrieg.core.api.identifier.NationIdentifier;
import com.riskrieg.core.api.identifier.TerritoryIdentifier;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

  public static Set<Claim> getClaimsOf(NationIdentifier identifier, Set<Claim> allClaims) {
    return allClaims.stream().filter(claim -> claim.identifier().equals(identifier)).collect(Collectors.toUnmodifiableSet());
  }

  public static Set<TerritoryIdentifier> getAllNeighbors(NationIdentifier identifier, Set<Claim> allClaims, GameMap map) {
    Set<TerritoryIdentifier> neighbors = new HashSet<>();
    Set<TerritoryIdentifier> myTerritories = getClaimsOf(identifier, allClaims).stream().map(Claim::territory).map(GameTerritory::identifier).collect(Collectors.toSet());
    for (TerritoryIdentifier id : myTerritories) {
      neighbors.addAll(map.neighborsAsIdentifiers(id));
    }
    neighbors.removeAll(myTerritories);
    return Collections.unmodifiableSet(neighbors);
  }

  public static Set<TerritoryIdentifier> getClaimableTerritories(NationIdentifier identifier, Set<Claim> allClaims, GameMap map) {
    Set<TerritoryIdentifier> neighbors = new HashSet<>(getAllNeighbors(identifier, allClaims, map));
    return Collections.unmodifiableSet(neighbors);
  }

  public static long getAllowedClaimsPerTurn(NationIdentifier identifier, Set<Claim> allClaims, GameConstants constants, GameMap map) {
    long allowedClaims = constants.initialClaimAmount() + (long) (Math.floor(getClaimsOf(identifier, allClaims).size() / (double) constants.claimIncreaseThreshold()));
    return Math.min(getClaimableTerritories(identifier, allClaims, map).size(), allowedClaims);
  }

}
