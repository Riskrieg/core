/*
 *     Riskrieg, an open-source conflict simulation game.
 *     Copyright (C) 2021 Aaron Yoder <aaronjyoder@gmail.com> and Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.riskrieg.core.api.game.entity.nation;

import com.riskrieg.core.api.game.GameConstants;
import com.riskrieg.core.api.game.territory.Claim;
import com.riskrieg.core.api.game.territory.GameTerritory;
import com.riskrieg.core.api.game.territory.TerritoryType;
import com.riskrieg.core.api.identifier.NationIdentifier;
import com.riskrieg.core.api.identifier.PlayerIdentifier;
import com.riskrieg.core.util.game.GameUtil;
import com.riskrieg.map.RkmMap;
import com.riskrieg.map.Territory;
import com.riskrieg.map.territory.TerritoryIdentity;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public record Nation(NationIdentifier identifier, int colorId, PlayerIdentifier leaderIdentifier) {

  public Nation {
    Objects.requireNonNull(identifier);
    Objects.requireNonNull(leaderIdentifier);
  }

  public Nation withLeader(PlayerIdentifier leaderIdentifier) {
    return new Nation(identifier, colorId, leaderIdentifier);
  }

  /**
   * Determines whether this nation has any claims at all
   *
   * @param claims The set of all claims
   * @return {@code true} if this nation has a claim on any territory, otherwise {@code false}
   */
  public boolean hasAnyClaim(Set<Claim> claims) {
    return claims.stream().anyMatch(claim -> claim.identifier().equals(identifier));
  }

  /**
   * Determines whether this nation has any claims on territory of the provided type
   *
   * @param claims The set of all claims
   * @param type   The territory type
   * @return {@code true} if this nation has a claim on any territory of the provided type, otherwise {@code false}
   */
  public boolean hasAnyClaim(Set<Claim> claims, TerritoryType type) {
    return claims.stream().anyMatch(claim -> claim.identifier().equals(identifier)
        && claim.territory().type().equals(type));
  }

  /**
   * Determines whether this nation has a claim on the provided territory
   *
   * @param identity The territory identifier
   * @param claims   The set of all claims
   * @return {@code true} if this nation has a claim on the provided territory, otherwise {@code false}
   */
  public boolean hasClaimOn(TerritoryIdentity identity, Set<Claim> claims) {
    return claims.stream().anyMatch(claim -> claim.identifier().equals(identifier)
        && claim.territory().identity().equals(identity));
  }

  /**
   * Determines whether this nation has a claim on the provided territory, and the territory is of the provided type
   *
   * @param identity The territory identifier
   * @param claims   The set of all claims
   * @param type     The territory type
   * @return {@code true} if this nation has a claim on the provided territory and the territory is of the provided type, otherwise {@code false}
   */
  public boolean hasClaimOn(TerritoryIdentity identity, Set<Claim> claims, TerritoryType type) {
    return claims.stream().anyMatch(claim -> claim.identifier().equals(identifier)
        && claim.territory().identity().equals(identity)
        && claim.territory().type().equals(type));
  }

  /**
   * Returns the set of claims that belong to this nation
   *
   * @param claims The set of all claims
   * @return the set of claims that belong to this nation
   */
  public Set<Claim> getClaimedTerritories(Set<Claim> claims) {
    return claims.stream().filter(claim -> claim.identifier().equals(identifier)).collect(Collectors.toUnmodifiableSet());
  }

  /**
   * Returns the set of all territories (as identifiers) that neighbor any of this nation's claims
   *
   * @param claims The set of all claims
   * @param map    The game map
   * @return the set of all territories (as identifiers) that neighbor any of this nation's claims
   */
  public Set<TerritoryIdentity> getNeighbors(Set<Claim> claims, RkmMap map) {
    Set<TerritoryIdentity> neighbors = new HashSet<>();
    Set<TerritoryIdentity> myTerritories = getClaimedTerritories(claims).stream().map(Claim::territory).map(GameTerritory::identity).collect(Collectors.toSet());
    for (TerritoryIdentity identity : myTerritories) {
      neighbors.addAll(GameUtil.getNeighbors(identity, map).stream().map(Territory::identity).collect(Collectors.toSet()));
    }
    neighbors.removeAll(myTerritories);
    return Collections.unmodifiableSet(neighbors);
  }

  /**
   * Determines whether the provided territory is neighboring any of this nation's claims
   *
   * @param identity The territory identifier
   * @param claims   The set of all claims
   * @param map      The game map
   * @return {@code true} if the provided territory neighbors any of this nation's claims, otherwise {@code false}
   */
  public boolean isTerritoryNeighboring(TerritoryIdentity identity, Set<Claim> claims, RkmMap map) {
    return getNeighbors(claims, map).contains(identity);
  }

  /**
   * Returns the set of territories (as identifiers) that this nation has the ability to claim
   *
   * @param claims The set of all claims
   * @param map    The game map
   * @return the set of all territories (as identifiers) that this nation has the ability to claim
   */
  public Set<TerritoryIdentity> getClaimableTerritories(Set<Claim> claims, RkmMap map, Set<Nation> allies) {
    Set<TerritoryIdentity> neighbors = new HashSet<>(getNeighbors(claims, map));
    neighbors.removeIf(identity -> allies.stream().anyMatch(ally -> ally.hasClaimOn(identity, claims)));
    return Collections.unmodifiableSet(neighbors);
  }

  /**
   * Returns the amount of claims this nation can make based on how many territories are already claimed.
   *
   * @param claims    The set of all claims
   * @param constants The game constants
   * @param map       The game map
   * @return the number claims that can be made as a {@code long}
   */
  public long getAllowedClaimAmount(Set<Claim> claims, GameConstants constants, RkmMap map, Set<Nation> allies) {
    long allowedClaims = constants.initialClaimAmount() + (long) (Math.floor(getClaimedTerritories(claims).size() / (double) constants.claimIncreaseThreshold()));
    return Math.min(getClaimableTerritories(claims, map, allies).size(), allowedClaims);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Nation nation = (Nation) o;
    return colorId == nation.colorId && identifier.equals(nation.identifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(identifier, colorId);
  }

}
