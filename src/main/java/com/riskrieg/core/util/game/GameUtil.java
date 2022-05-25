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

  /* Nation-related utilities */

  /**
   * Determines whether the provided nation has any claims on any territory.
   *
   * @param identifier The identifier of the nation
   * @param allClaims  The set of claims to search through
   * @return {@code true} if the provided nation has any claims, otherwise {@code false}
   */
  public static boolean nationClaimsAnyTerritory(NationIdentifier identifier, Set<Claim> allClaims) {
    return allClaims.stream().anyMatch(claim -> claim.identifier().equals(identifier));
  }

  /**
   * Determines whether the provided nation has any claims on any territory of the provided type.
   *
   * @param identifier The identifier of the nation
   * @param allClaims  The set of claims to search through
   * @param type       The type of the territory
   * @return {@code true} if provided nation has any claims of the given type, otherwise {@code false}
   */
  public static boolean nationClaimsAnyTerritory(NationIdentifier identifier, Set<Claim> allClaims, TerritoryType type) {
    return allClaims.stream().anyMatch(claim -> claim.identifier().equals(identifier)
        && claim.territory().type().equals(type));
  }

  /**
   * Determines whether the provided nation has a claim on the provided territory.
   *
   * @param identifier          The identifier of the nation
   * @param territoryIdentifier The identifier of the territory
   * @param allClaims           The set of claims to search through
   * @return {@code true} if the provided nation has a claim on the provided territory, otherwise {@code false}
   */
  public static boolean nationClaimsTerritory(NationIdentifier identifier, TerritoryIdentifier territoryIdentifier, Set<Claim> allClaims) {
    return allClaims.stream().anyMatch(claim -> claim.identifier().equals(identifier)
        && claim.territory().identifier().equals(territoryIdentifier));
  }

  /**
   * Determines whether the provided nation has a claim on the provided territory, and the territory is of the provided type.
   *
   * @param identifier          The identifier of the nation
   * @param territoryIdentifier The identifier of the territory
   * @param allClaims           The set of claims to search through
   * @param type                The type of the territory
   * @return {@code true} if the provided nation has a claim on the provided territory and the provided territory is of the provided type, otherwise {@code false}
   */
  public static boolean nationClaimsTerritory(NationIdentifier identifier, TerritoryIdentifier territoryIdentifier, Set<Claim> allClaims, TerritoryType type) {
    return allClaims.stream().anyMatch(claim -> claim.identifier().equals(identifier)
        && claim.territory().identifier().equals(territoryIdentifier)
        && claim.territory().type().equals(type));
  }

  /**
   * Gets the number of territories a nation has claimed.
   *
   * @param identifier The identifier of the nation
   * @param allClaims  The set of claims to search through
   * @return the number of territories the provided nation has claimed as a {@code long}
   */
  public static long getTerritorialClaimCount(NationIdentifier identifier, Set<Claim> allClaims) {
    return allClaims.stream().filter(claim -> claim.identifier().equals(identifier)).count();
  }

  /**
   * Gets the set of claims a nation has.
   *
   * @param identifier The identifier of the nation
   * @param allClaims  The set of claims to search through
   * @return the set of claims the provided nation has
   */
  public static Set<Claim> getClaimSet(NationIdentifier identifier, Set<Claim> allClaims) {
    return allClaims.stream().filter(claim -> claim.identifier().equals(identifier)).collect(Collectors.toUnmodifiableSet());
  }

  /**
   * Gets the set of all neighboring territory identifiers for the provided nation.
   *
   * @param identifier The identifier of the nation
   * @param allClaims  The set of claims to build the nation's claims from
   * @param map        The map to look for neighbors on
   * @return The set of all territory identifiers representing neighbors to the provided nation.
   */
  public static Set<TerritoryIdentifier> getAllNeighbors(NationIdentifier identifier, Set<Claim> allClaims, GameMap map) {
    Set<TerritoryIdentifier> neighbors = new HashSet<>();
    Set<TerritoryIdentifier> myTerritories = getClaimSet(identifier, allClaims).stream().map(Claim::territory).map(GameTerritory::identifier).collect(Collectors.toSet());
    for (TerritoryIdentifier id : myTerritories) {
      neighbors.addAll(map.neighborsAsIdentifiers(id));
    }
    neighbors.removeAll(myTerritories);
    return Collections.unmodifiableSet(neighbors);
  }

  /**
   * Gets the set of all territories that can be claimed by the provided nation.
   *
   * @param identifier The identifier of the nation
   * @param allClaims  The set of claims to build the nation's claims from
   * @param map        The map to look for claimable territory on
   * @return The set of all territory identifiers representing territories the provided nation can claim
   */
  public static Set<TerritoryIdentifier> getClaimableTerritorySet(NationIdentifier identifier, Set<Claim> allClaims, GameMap map) {
    Set<TerritoryIdentifier> neighbors = new HashSet<>(getAllNeighbors(identifier, allClaims, map));
    return Collections.unmodifiableSet(neighbors);
  }

  /**
   * Gets the number of territories that the provided nation can claim per turn. This differs from {@code getClaimableTerritorySet#size}, in that it accounts for the fact that the
   * number of allowed claims is not always the same size as the amount of territories that can be claimed.
   *
   * @param identifier The identifier of the nation
   * @param allClaims  The set of claims to build the nation's claims from
   * @param constants  The game constants to determine claim amounts from
   * @param map        The map to look for claimable territory on
   * @return the number territories that can be claimed as a {@code long}
   */
  public static long getAllowedClaimsPerTurn(NationIdentifier identifier, Set<Claim> allClaims, GameConstants constants, GameMap map) {
    long allowedClaims = constants.initialClaimAmount() + (long) (Math.floor(getClaimSet(identifier, allClaims).size() / (double) constants.claimIncreaseThreshold()));
    return Math.min(getClaimableTerritorySet(identifier, allClaims, map).size(), allowedClaims);
  }

}
