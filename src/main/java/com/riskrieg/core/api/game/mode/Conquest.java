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

package com.riskrieg.core.api.game.mode;

import com.riskrieg.codec.decode.RkmDecoder;
import com.riskrieg.core.api.game.Attack;
import com.riskrieg.core.api.game.ClaimOverride;
import com.riskrieg.core.api.game.EndReason;
import com.riskrieg.core.api.game.Game;
import com.riskrieg.core.api.game.GameConstants;
import com.riskrieg.core.api.game.GamePhase;
import com.riskrieg.core.api.game.Save;
import com.riskrieg.core.api.game.entity.alliance.Alliance;
import com.riskrieg.core.api.game.entity.nation.Nation;
import com.riskrieg.core.api.game.entity.player.Player;
import com.riskrieg.core.api.game.event.AllianceEvent;
import com.riskrieg.core.api.game.event.ClaimEvent;
import com.riskrieg.core.api.game.event.UpdateEvent;
import com.riskrieg.core.api.game.feature.Feature;
import com.riskrieg.core.api.game.feature.FeatureFlag;
import com.riskrieg.core.api.game.feature.alliance.AllianceStatus;
import com.riskrieg.core.api.game.order.TurnOrder;
import com.riskrieg.core.api.game.territory.Claim;
import com.riskrieg.core.api.game.territory.GameTerritory;
import com.riskrieg.core.api.game.territory.TerritoryType;
import com.riskrieg.core.api.identifier.GameIdentifier;
import com.riskrieg.core.api.identifier.NationIdentifier;
import com.riskrieg.core.api.identifier.PlayerIdentifier;
import com.riskrieg.core.api.requests.GameAction;
import com.riskrieg.core.internal.requests.GenericAction;
import com.riskrieg.core.util.game.GameUtil;
import com.riskrieg.map.RkmMap;
import com.riskrieg.map.territory.TerritoryIdentity;
import com.riskrieg.palette.RkpColor;
import com.riskrieg.palette.RkpPalette;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public final class Conquest implements Game {

  // Immutable
  private final GameIdentifier identifier;
  private final GameConstants constants;
  private final Instant creationTime;
  private final Set<Nation> nations;
  private final Set<Claim> claims;
  private final Set<Alliance> alliances;

  private final Set<FeatureFlag> featureFlags;

  // Mutable
  private RkpPalette palette;
  private Instant updatedTime;

  private Deque<Player> players;

  private GamePhase phase;
  private RkmMap map; // Nullable

  public Conquest(Save save, Path mapRepository) {
    if (save.palette().size() != save.constants().maximumPlayers()) {
      throw new IllegalStateException("The maximum number of players must equal the amount of colors provided");
    }
    this.identifier = save.identifier();
    this.constants = save.constants();
    this.palette = save.palette();
    this.creationTime = save.creationTime().toInstant();
    this.updatedTime = save.updatedTime().toInstant();
    this.phase = save.phase();
    if (!save.mapCodename().isBlank()) {
      RkmDecoder decoder = new RkmDecoder();
      try {
        this.map = decoder.decode(mapRepository.resolve(save.mapCodename() + ".rkm"));
      } catch (IOException | NoSuchAlgorithmException e) {
        throw new RuntimeException(e); // Panic, map can't be loaded.
      }
    }
    this.players = new ArrayDeque<>(save.players());
    this.nations = save.nations();
    this.claims = save.claims();
    this.alliances = save.alliances();
    this.featureFlags = save.featureFlags();
  }

  public Conquest(GameIdentifier identifier, GameConstants constants, RkpPalette palette, FeatureFlag... featureFlags) {
    if (palette.size() < constants.maximumPlayers()) {
      throw new IllegalStateException("The provided palette only supports up to " + palette.size()
          + " colors, but the provided constants allows a maximum amount of " + constants.maximumPlayers() + " players.");
    }
    if (palette.size() > constants.maximumPlayers()) {
      throw new IllegalStateException("The provided palette supports up to " + palette.size()
          + " colors, but the provided constants only allows a maximum amount of " + constants.maximumPlayers() + " players.");
    }
    this.identifier = identifier;
    this.constants = constants;
    this.palette = palette;
    this.creationTime = Instant.now();
    this.updatedTime = Instant.now();
    this.phase = GamePhase.SETUP;
    this.players = new ArrayDeque<>();
    this.nations = new HashSet<>();
    this.claims = new HashSet<>();
    this.alliances = new HashSet<>();
    this.featureFlags = new HashSet<>();
  }

  @NonNull
  @Override
  public GameIdentifier identifier() {
    return identifier;
  }

  @NonNull
  @Override
  public GameConstants constants() {
    return constants;
  }

  @NonNull
  @Override
  public RkpPalette palette() {
    return palette;
  }

  @NonNull
  @Override
  public Instant creationTime() {
    return creationTime;
  }

  @NonNull
  @Override
  public Instant updatedTime() {
    return updatedTime;
  }

  @NonNull
  @Override
  public GamePhase phase() {
    return phase;
  }

  @Override
  public RkmMap map() {
    return map;
  }

  @NonNull
  @Override
  public Collection<Player> players() {
    return Collections.unmodifiableCollection(players);
  }

  @NonNull
  @Override
  public Set<Nation> nations() {
    return Collections.unmodifiableSet(nations);
  }

  @NonNull
  @Override
  public Set<Claim> claims() {
    return Collections.unmodifiableSet(claims);
  }

  @NonNull
  @Override
  public Set<Alliance> alliances() {
    return Collections.unmodifiableSet(alliances);
  }

  @Override
  public Set<FeatureFlag> featureFlags() {
    return Collections.unmodifiableSet(featureFlags);
  }

  @Override
  public boolean isFeatureEnabled(Feature feature) {
    for (FeatureFlag flag : featureFlags) {
      if (flag.feature() == feature) {
        return flag.enabled();
      }
    }
    return false;
  }

  @NonNull
  @Override
  public Optional<Player> getCurrentPlayer() {
    return switch (phase) {
      default -> Optional.empty();
      case ACTIVE -> Optional.of(players.getFirst());
    };
  }

  @NonNull
  @Override
  public Optional<Nation> getCurrentNation() {
    return switch (phase) {
      default -> Optional.empty();
      case ACTIVE -> getNation(players.getFirst().identifier());
    };
  }

  @NonNull
  @Override
  public GameAction<Boolean> setPalette(RkpPalette palette) {
    Objects.requireNonNull(palette);
    try {
      return switch (phase) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case SETUP, ACTIVE -> {
          if (palette.equals(this.palette)) {
            throw new IllegalStateException("The provided palette is identical to the current palette, so no changes were made.");
          }
          if (palette.size() < constants.maximumPlayers()) {
            throw new IllegalStateException("The provided palette only supports up to " + palette.size()
                + " colors, but the provided constants allows a maximum amount of " + constants.maximumPlayers() + " players.");
          }
          if (palette.size() > constants.maximumPlayers()) {
            throw new IllegalStateException("The provided palette supports up to " + palette.size()
                + " colors, but the provided constants only allows a maximum amount of " + constants.maximumPlayers() + " players.");
          }
          if (palette.size() != this.palette.size()) {
            throw new IllegalStateException("The provided palette must have the same number of colors defined as the current palette, which is " + this.palette.size() + ".");
          }
          this.palette = palette;
          yield new GenericAction<>(true);
        }
      };
    } catch (Exception e) {
      return new GenericAction<>(false, e);
    }
  }

  @NonNull
  @Override
  public GameAction<RkmMap> selectMap(RkmMap map) {
    Objects.requireNonNull(map);
    this.updatedTime = Instant.now();
    try {
      return switch (phase) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case ACTIVE -> throw new IllegalStateException("The map can only be set during the setup phase");
        case SETUP -> {
          this.map = map;
          claims.clear();
          yield new GenericAction<>(map);
        }
      };
    } catch (Exception e) {
      return new GenericAction<>(e);
    }
  }

  @NonNull
  @Override
  public GameAction<Player> addPlayer(PlayerIdentifier identifier, String name) {
    Objects.requireNonNull(identifier);
    Objects.requireNonNull(name);
    this.updatedTime = Instant.now();
    try {
      return switch (phase) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case ACTIVE -> throw new IllegalStateException("Players can only be added during the setup phase");
        case SETUP -> {
          Player player = new Player(identifier, name);
          if (players.contains(player)) {
            throw new IllegalArgumentException("A player with that identifier is already in the game");
          }
          if (players.size() >= constants.maximumPlayers()) {
            throw new IllegalStateException("The player could not be added because the game is full");
          }
          if (players.add(player)) {
            yield new GenericAction<>(player);
          } else {
            throw new IllegalStateException("The player could not be added for an unknown reason");
          }
        }
      };
    } catch (Exception e) {
      return new GenericAction<>(e);
    }
  }

  @NonNull
  @Override
  public GameAction<Boolean> removePlayer(PlayerIdentifier identifier) {
    Objects.requireNonNull(identifier);
    this.updatedTime = Instant.now();
    try {
      return switch (phase) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case SETUP, ACTIVE -> {
          if (players.stream().noneMatch(player -> player.identifier().equals(identifier))) {
            throw new IllegalStateException("That player cannot be removed because they are not in the game");
          }

          nations.stream().filter(nation -> nation.leaderIdentifier().equals(identifier))
              .forEach(nation -> {
                    claims.removeIf(claim -> claim.identifier().equals(nation.identifier()));
                    alliances.removeIf(alliance -> alliance.ally() == nation.identifier() || alliance.coally() == nation.identifier());
                  }
              );
          nations.removeIf(nation -> nation.leaderIdentifier().equals(identifier));
          players.removeIf(player -> player.identifier().equals(identifier));

          yield new GenericAction<>(true);
        }
      };
    } catch (Exception e) {
      return new GenericAction<>(false, e);
    }
  }

  @NonNull
  @Override
  public GameAction<Nation> createNation(RkpColor color, PlayerIdentifier identifier) {
    Objects.requireNonNull(color);
    Objects.requireNonNull(identifier);
    this.updatedTime = Instant.now();
    try {
      return switch (phase) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case ACTIVE -> throw new IllegalStateException("Nations can only be created in the setup phase");
        case SETUP -> {
          Nation nation = new Nation(NationIdentifier.uuid(), color.order(), identifier);
          if (players.stream().noneMatch(p -> p.identifier().equals(identifier))) {
            throw new IllegalStateException("A player must join the game before creating a nation");
          }
          if (nations.size() >= palette.size()) {
            throw new IllegalStateException("The nation could not be formed because the maximum number of nations has already been reached");
          }
          if (nations.stream().anyMatch(n -> n.leaderIdentifier().equals(identifier))) {
            throw new IllegalStateException("That player is already in another nation");
          }
          if (nations.stream().anyMatch(n -> n.colorId() == color.order())) {
            throw new IllegalStateException("A nation with that color is already in the game");
          }
          nations.add(nation);
          yield new GenericAction<>(nation);
        }
      };
    } catch (Exception e) {
      return new GenericAction<>(e);
    }
  }

  @NonNull
  @Override
  public GameAction<Boolean> dissolveNation(RkpColor color) {
    Objects.requireNonNull(color);
    return new GenericAction<>(false, new IllegalAccessException("The dissolve nation action is unavailable in this game mode."));
  }

  @NonNull
  @Override
  public GameAction<ClaimEvent> claim(Attack attack, NationIdentifier identifier, ClaimOverride override, TerritoryIdentity... territories) {
    Objects.requireNonNull(attack);
    Objects.requireNonNull(identifier);
    Objects.requireNonNull(override);
    Objects.requireNonNull(territories);
    this.updatedTime = Instant.now();
    try {
      return switch (phase) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case ACTIVE -> {
          if (map == null) {
            throw new IllegalStateException("A valid map must be selected before claiming territories");
          }
          Optional<Nation> optNation = getNation(identifier);
          if (optNation.isEmpty()) {
            throw new IllegalStateException("That nation does not exist");
          }
          Nation attacker = optNation.get();
          Optional<Player> optPlayer = getPlayer(attacker.leaderIdentifier());
          if (optPlayer.isEmpty()) {
            throw new IllegalStateException("That player does not exist");
          }
          Player leader = optPlayer.get();
          if (!players.getFirst().identifier().equals(attacker.leaderIdentifier())) {
            throw new IllegalStateException("It is not that player's turn");
          }

          Set<TerritoryIdentity> territoriesToClaim = new HashSet<>(Arrays.asList(territories));
          var invalidTerritories = territoriesToClaim.stream().filter(identity -> GameUtil.territoryNotExists(identity, map)).toList();
          if (!invalidTerritories.isEmpty()) {
            throw new IllegalStateException("The following territories do not exist or are otherwise invalid: "
                + invalidTerritories.stream().map(TerritoryIdentity::toString).collect(Collectors.joining(", ")).trim());
          }
          var alreadyClaimedTerritories = territoriesToClaim.stream().filter(identity -> attacker.hasClaimOn(identity, claims)).toList();
          if (!alreadyClaimedTerritories.isEmpty()) {
            throw new IllegalStateException("The following territories are already claimed by you: "
                + alreadyClaimedTerritories.stream().map(TerritoryIdentity::toString).collect(Collectors.joining(", ")).trim());
          }
          var notNeighboringTerritories = territoriesToClaim.stream().filter(identity -> attacker.isTerritoryNeighboring(identity, claims, map)).toList();
          if (!notNeighboringTerritories.isEmpty()) {
            throw new IllegalStateException("The following territories are not neighboring your nation: "
                + notNeighboringTerritories.stream().map(TerritoryIdentity::toString).collect(Collectors.joining(", ")).trim());
          }
          if (isFeatureEnabled(Feature.ALLIANCES)) {
            List<TerritoryIdentity> allyTerritories = new ArrayList<>();
            for (Nation n : nations) {
              if (allianceStatus(attacker.identifier(), n.identifier()) == AllianceStatus.COMPLETE) {
                Set<TerritoryIdentity> nTerritories = n.getClaimedTerritories(claims).stream().map(Claim::territory).map(GameTerritory::identity).collect(Collectors.toSet());
                allyTerritories.addAll(territoriesToClaim.stream().filter(nTerritories::contains).collect(Collectors.toSet()));
              }
            }

            if (!allyTerritories.isEmpty()) {
              throw new IllegalStateException("The following territories belong to allies: "
                  + allyTerritories.stream().map(TerritoryIdentity::toString).collect(Collectors.joining(", ")).trim());
            }
          }

          long allowedClaimAmount = attacker.getAllowedClaimAmount(claims, constants, map, getAllies(attacker.identifier()));
          if (allowedClaimAmount == 0) {
            throw new IllegalStateException("Trying to claim " + territoriesToClaim.size() + (territoriesToClaim.size() == 1 ? " territory" : " territories")
                + " but unable to claim any territories.");
          }

          switch (override) {
            case NONE -> {
              if (territoriesToClaim.size() != allowedClaimAmount) { // If there's no override, must match exactly.
                throw new IllegalStateException("Trying to claim " + territoriesToClaim.size() + (territoriesToClaim.size() == 1 ? " territory" : " territories")
                    + " but must claim " + allowedClaimAmount + (allowedClaimAmount == 1 ? " territory" : " territories"));
              }
            }
            case AUTO -> {
              if (territoriesToClaim.size() > allowedClaimAmount) { // Invalid state
                throw new IllegalStateException("Trying to claim " + territoriesToClaim.size() + (territoriesToClaim.size() == 1 ? " territory" : " territories")
                    + " but cannot claim more than " + allowedClaimAmount + (allowedClaimAmount == 1 ? " territory" : " territories"));
              } else if (territoriesToClaim.size() < allowedClaimAmount) { // Add random territories to fill out the rest
                var claimable = new ArrayList<>(attacker.getClaimableTerritories(claims, map, getAllies(attacker.identifier())));
                claimable.removeAll(territoriesToClaim);
                Collections.shuffle(claimable);
                claimable.stream()
                    .limit(allowedClaimAmount - territoriesToClaim.size())
                    .forEach(territoriesToClaim::add);

                if (territoriesToClaim.size() != allowedClaimAmount) { // Check to make sure it matches exactly, just for sanity
                  throw new IllegalStateException("Auto: Trying to claim " + territoriesToClaim.size() + (territoriesToClaim.size() == 1 ? " territory" : " territories")
                      + " but must claim " + allowedClaimAmount + (allowedClaimAmount == 1 ? " territory" : " territories"));
                }
              }
            }
            case EXACT -> {
              if (territoriesToClaim.size() == 0) {
                throw new IllegalStateException("Trying to claim 0 territories but must claim at least one territory");
              }
              if (territoriesToClaim.size() > allowedClaimAmount) { // Invalid state
                throw new IllegalStateException("Trying to claim " + territoriesToClaim.size() + (territoriesToClaim.size() == 1 ? " territory" : " territories")
                    + " but cannot claim more than " + allowedClaimAmount + (allowedClaimAmount == 1 ? " territory" : " territories"));
              }
            }
          }

          Set<Claim> freeClaims = new HashSet<>();
          Set<Claim> wonClaims = new HashSet<>();
          Set<Claim> defendedClaims = new HashSet<>();

          for (TerritoryIdentity attackedTerritory : territoriesToClaim) {
            Optional<Nation> defendingNation = getNation(attackedTerritory);
            if (defendingNation.isPresent()) {
              Nation defender = defendingNation.get();
              if (attack.success(attacker, defender, attackedTerritory, map, claims, constants)) { // Attacker wins, so the claim is transferred to the attacker
                boolean wasCapital = defender.hasClaimOn(attackedTerritory, claims, TerritoryType.CAPITAL);
                claims.removeIf(claim -> claim.identifier().equals(defender.identifier()) && claim.territory().identity().equals(attackedTerritory));
                Set<Claim> defenderClaims = defender.getClaimedTerritories(claims);
                if (wasCapital && defenderClaims.size() > 0) { // Make one of the defender's territories its new capital
                  Optional<Claim> randomClaim = defenderClaims.stream().skip(new Random().nextInt(defenderClaims.size())).findFirst();
                  randomClaim.ifPresent(claim -> {
                    claims.remove(claim);
                    claims.add(new Claim(claim.identifier(), claim.territory().with(TerritoryType.CAPITAL)));
                  });
                }
                claims.add(new Claim(attacker.identifier(), new GameTerritory(attackedTerritory)));
                getClaim(attackedTerritory).ifPresent(wonClaims::add);
              } else { // Attacker lost, so nothing happens
                getClaim(attackedTerritory).ifPresent(defendedClaims::add);
              }
            } else { // Nobody owns it, so it's automatically claimed
              getClaim(attackedTerritory).ifPresent(freeClaims::add);
            }
          }

          yield new GenericAction<>(new ClaimEvent(attacker, leader, freeClaims, wonClaims, defendedClaims));
        }
        case SETUP -> {
          if (map == null) {
            throw new IllegalStateException("A valid map must be selected before claiming territories");
          }
          if (territories.length != 1) {
            throw new IllegalStateException("Exactly one territory must be claimed during the setup phase.");
          }
          TerritoryIdentity territory = territories[0];
          Optional<Nation> optNation = getNation(identifier);
          if (optNation.isEmpty()) {
            throw new IllegalStateException("That nation does not exist");
          }
          Nation attacker = optNation.get();
          Optional<Player> optPlayer = getPlayer(attacker.leaderIdentifier());
          if (optPlayer.isEmpty()) {
            throw new IllegalStateException("That player does not exist");
          }
          Player leader = optPlayer.get();
          if (GameUtil.territoryNotExists(territory, map)) {
            throw new IllegalStateException("That territory does not exist on the current map");
          }
          if (GameUtil.territoryIsClaimed(territory, claims)) {
            throw new IllegalStateException("That territory is already claimed by someone else");
          }
          if (attacker.hasAnyClaim(claims, TerritoryType.CAPITAL)) {
            claims.removeIf(claim -> claim.identifier().equals(identifier) && claim.territory().type().equals(TerritoryType.CAPITAL));
          }
          var freeClaim = new Claim(identifier, new GameTerritory(territory, TerritoryType.CAPITAL));
          claims.add(freeClaim);
          yield new GenericAction<>(new ClaimEvent(attacker, leader, Set.of(freeClaim)));
        }
      };
    } catch (Exception e) {
      return new GenericAction<>(e);
    }
  }

  @NonNull
  @Override
  public GameAction<Boolean> unclaim(NationIdentifier identifier, TerritoryIdentity... territories) {
    Objects.requireNonNull(identifier);
    Objects.requireNonNull(territories);
    this.updatedTime = Instant.now();
    try {
      return switch (phase) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case ACTIVE -> {
          if (map == null) {
            throw new IllegalStateException("A valid map must be selected before unclaiming territories");
          }
          Optional<Nation> optionalNation = getNation(identifier);
          if (optionalNation.isEmpty()) {
            throw new IllegalStateException("That nation does not exist");
          }
          Nation nation = optionalNation.get();
          if (!players.getFirst().identifier().equals(nation.leaderIdentifier())) {
            throw new IllegalStateException("It is not that player's turn");
          }
          if (!nation.hasAnyClaim(claims)) {
            throw new IllegalStateException("Cannot unclaim because this nation has no claims");
          }
          Set<TerritoryIdentity> territoriesToUnclaim = new HashSet<>(Arrays.asList(territories));
          if (territoriesToUnclaim.isEmpty()) {
            throw new IllegalStateException("Nothing to unclaim");
          }
          var invalidTerritories = territoriesToUnclaim.stream().filter(territoryIdentifier -> GameUtil.territoryNotExists(territoryIdentifier, map)).toList();
          if (!invalidTerritories.isEmpty()) {
            throw new IllegalStateException("The following territories do not exist or are otherwise invalid: "
                + invalidTerritories.stream().map(TerritoryIdentity::toString).collect(Collectors.joining(", ")).trim());
          }
          var notClaimedTerritories = territoriesToUnclaim.stream().filter(territoryIdentifier -> !nation.hasClaimOn(territoryIdentifier, claims)).toList();
          if (!notClaimedTerritories.isEmpty()) {
            throw new IllegalStateException("The following territories are not claimed by you: "
                + notClaimedTerritories.stream().map(TerritoryIdentity::toString).collect(Collectors.joining(", ")).trim());
          }
          for (TerritoryIdentity identity : territoriesToUnclaim) {
            claims.removeIf(claim -> claim.territory().identity().equals(identity));
          }
          yield new GenericAction<>(true);
        }
        case SETUP -> {
          if (map == null) {
            throw new IllegalStateException("A valid map must be selected before unclaiming territories");
          }
          Optional<Nation> optionalNation = getNation(identifier);
          if (optionalNation.isEmpty()) {
            throw new IllegalStateException("That nation does not exist");
          }
          Nation nation = optionalNation.get();
          if (!nation.hasAnyClaim(claims)) {
            throw new IllegalStateException("Cannot unclaim because this nation has no claims");
          }
          Set<TerritoryIdentity> territoriesToUnclaim = new HashSet<>(Arrays.asList(territories));
          if (territoriesToUnclaim.isEmpty()) {
            throw new IllegalStateException("Nothing to unclaim");
          }
          var invalidTerritories = territoriesToUnclaim.stream().filter(territoryIdentifier -> GameUtil.territoryNotExists(territoryIdentifier, map)).toList();
          if (!invalidTerritories.isEmpty()) {
            throw new IllegalStateException("The following territories do not exist or are otherwise invalid: "
                + invalidTerritories.stream().map(TerritoryIdentity::toString).collect(Collectors.joining(", ")).trim());
          }
          var notClaimedTerritories = territoriesToUnclaim.stream().filter(territoryIdentifier -> !nation.hasClaimOn(territoryIdentifier, claims)).toList();
          if (!notClaimedTerritories.isEmpty()) {
            throw new IllegalStateException("The following territories are not claimed by you: "
                + notClaimedTerritories.stream().map(TerritoryIdentity::toString).collect(Collectors.joining(", ")).trim());
          }
          for (TerritoryIdentity identity : territoriesToUnclaim) {
            claims.removeIf(claim -> claim.territory().identity().equals(identity));
          }
          yield new GenericAction<>(true);
        }
      };
    } catch (Exception e) {
      return new GenericAction<>(false, e);
    }
  }

  @NonNull
  @Override
  public GameAction<Player> start(TurnOrder order, boolean reverse, boolean randomizeStart) {
    Objects.requireNonNull(order);
    this.updatedTime = Instant.now();
    try {
      return switch (phase) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case ACTIVE -> throw new IllegalStateException("A game can only be started in the setup phase");
        case SETUP -> {
          if (players.size() < constants.minimumPlayers()) {
            throw new IllegalStateException("A minimum of " + constants.minimumPlayers() + " players is required to play");
          }
          if (map == null) {
            throw new IllegalStateException("A valid map must be selected before starting the game");
          }
          if (nations.size() < players.size()) {
            throw new IllegalStateException("All players must form a nation");
          }
          if (nations.size() > players.size()) {
            throw new IllegalStateException("Critical error: Too many nations for the amount of players. Please report this as a bug");
          }
          if (!nations.stream().allMatch(nation -> nation.getClaimedTerritories(claims).size() == 1)) {
            throw new IllegalStateException("All nations must claim exactly one territory.");
          }
          this.players = order.getSorted(players, nations);
          if (reverse) {
            this.players = TurnOrder.reverse(this.players);
          }
          if (randomizeStart) {
            this.players = TurnOrder.randomizeStart(this.players);
          }
          this.phase = GamePhase.ACTIVE;
          yield new GenericAction<>(players.getFirst());
        }
      };
    } catch (Exception e) {
      return new GenericAction<>(e);
    }
  }

  @NonNull
  @Override
  public GameAction<UpdateEvent> update(boolean advanceTurn) {
    this.updatedTime = Instant.now();
    try {
      return switch (phase) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case ACTIVE -> {
          Optional<Player> previous = players.isEmpty() ? Optional.empty() : Optional.of(players.getFirst());
          /* Defeated Check */
          Set<Player> defeated = new HashSet<>();
          for (Nation nation : nations) {
            if (nation.getClaimedTerritories(claims).size() == 0) {
              getPlayer(nation.leaderIdentifier()).ifPresent(defeated::add);
            }
          }
          defeated.forEach(player -> removePlayer(player.identifier()).complete());

          EndReason endReason = EndReason.NONE;
          /* End State Check */
          if (players.size() == 0) {
            endReason = EndReason.NO_PLAYERS;
            phase = GamePhase.ENDED;
          } else if (players.size() == 1) {
            endReason = EndReason.DEFEAT;
            phase = GamePhase.ENDED;
          } else if (isFeatureEnabled(Feature.ALLIANCES) && nations.stream().allMatch(nation ->
              nation.getAllowedClaimAmount(claims, constants, map, getAllies(nation.identifier())) == 0)) {
            endReason = EndReason.ALLIED_VICTORY;
            phase = GamePhase.ENDED;
          } else if (nations.stream().allMatch(nation -> nation.getAllowedClaimAmount(claims, constants, map, getAllies(nation.identifier())) == 0)) {
            endReason = EndReason.STALEMATE;
            phase = GamePhase.ENDED;
          }

          if (advanceTurn) {
            players.addLast(players.removeFirst());
          }
          yield new GenericAction<>(new UpdateEvent(players.isEmpty() ? Optional.empty() : Optional.of(players.getFirst()), previous, defeated, endReason));
        }
        case SETUP -> new GenericAction<>(new UpdateEvent(Optional.empty(), Optional.empty(), new HashSet<>(), EndReason.NONE));
      };
    } catch (Exception e) {
      return new GenericAction<>(e);
    }
  }

  @Override
  public GameAction<AllianceEvent> ally(NationIdentifier ally, NationIdentifier coally) {
    this.updatedTime = Instant.now();
    try {
      if (!isFeatureEnabled(Feature.ALLIANCES)) {
        throw new IllegalStateException("Alliances are not enabled in this game");
      }
      return switch (phase) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case ACTIVE -> {
          var allyNationOpt = getNation(ally);
          var coallyNationOpt = getNation(coally);
          if (allyNationOpt.isEmpty() || coallyNationOpt.isEmpty()) {
            throw new IllegalStateException("One of the players provided is not in the game.");
          }
          Nation allyNation = allyNationOpt.get();
          Nation coallyNation = coallyNationOpt.get();

          var allyLeaderOpt = getPlayer(allyNation.leaderIdentifier());
          var coallyLeaderOpt = getPlayer(coallyNation.leaderIdentifier());
          if (allyLeaderOpt.isEmpty() || coallyLeaderOpt.isEmpty()) {
            throw new IllegalStateException("One of the players provided is not in the game.");
          }
          Player allyLeader = allyLeaderOpt.get();
          Player coallyLeader = coallyLeaderOpt.get();

          if (ally == coally) {
            throw new IllegalStateException("You cannot enter an alliance with yourself.");
          }

          alliances.add(new Alliance(ally, coally));
          AllianceStatus status = allianceStatus(ally, coally);

          EndReason reason = EndReason.NONE;

          if (nations.stream().allMatch(nation -> nation.getAllowedClaimAmount(claims, constants, map, getAllies(nation.identifier())) == 0)) {
            reason = EndReason.ALLIED_VICTORY;
            phase = GamePhase.ENDED;
          }

          yield new GenericAction<>(new AllianceEvent(allyNation, allyLeader, coallyNation, coallyLeader, status, reason));
        }
        case SETUP -> throw new IllegalStateException("Alliances can only be made after the game has started");
      };
    } catch (Exception e) {
      return new GenericAction<>(e);
    }
  }

  @Override
  public GameAction<AllianceEvent> unally(NationIdentifier ally, NationIdentifier coally) {
    this.updatedTime = Instant.now();
    try {
      if (!isFeatureEnabled(Feature.ALLIANCES)) {
        throw new IllegalStateException("Alliances are not enabled in this game");
      }
      return switch (phase) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case ACTIVE -> {
          var allyNationOpt = getNation(ally);
          var coallyNationOpt = getNation(coally);
          if (allyNationOpt.isEmpty() || coallyNationOpt.isEmpty()) {
            throw new IllegalStateException("One of the players provided is not in the game.");
          }
          Nation allyNation = allyNationOpt.get();
          Nation coallyNation = coallyNationOpt.get();

          var allyLeaderOpt = getPlayer(allyNation.leaderIdentifier());
          var coallyLeaderOpt = getPlayer(coallyNation.leaderIdentifier());
          if (allyLeaderOpt.isEmpty() || coallyLeaderOpt.isEmpty()) {
            throw new IllegalStateException("One of the players provided is not in the game.");
          }
          Player allyLeader = allyLeaderOpt.get();
          Player coallyLeader = coallyLeaderOpt.get();

          if (ally == coally) {
            throw new IllegalStateException("You cannot break an alliance with yourself.");
          }

          AllianceStatus oldStatus = allianceStatus(ally, coally);
          if (oldStatus == AllianceStatus.COMPLETE) {
            alliances.removeIf(alliance -> (alliance.ally() == ally && alliance.coally() == coally) || (alliance.ally() == coally && alliance.coally() == ally));
          } else {
            alliances.removeIf(alliance -> alliance.ally() == ally && alliance.coally() == coally);
          }

          AllianceStatus status = allianceStatus(ally, coally);
          yield new GenericAction<>(new AllianceEvent(allyNation, allyLeader, coallyNation, coallyLeader, status, EndReason.NONE));
        }
        case SETUP -> throw new IllegalStateException("Alliances can only be broken after the game has started");
      };
    } catch (Exception e) {
      return new GenericAction<>(e);
    }
  }

  @Override
  public AllianceStatus allianceStatus(NationIdentifier ally, NationIdentifier coally) {
    this.updatedTime = Instant.now();
    if (!isFeatureEnabled(Feature.ALLIANCES)) {
      return AllianceStatus.NONE;
    }
    return switch (phase) {
      case ENDED, SETUP -> AllianceStatus.NONE;
      case ACTIVE -> {
        AllianceStatus result = AllianceStatus.NONE;

        for (Alliance alliance : alliances) {
          if (result == AllianceStatus.COMPLETE) {
            break;
          }
          result = switch (result) {
            case NONE -> {
              if (alliance.ally() == ally && alliance.coally() == coally) {
                yield AllianceStatus.SENT;
              } else if (alliance.ally() == coally && alliance.coally() == ally) {
                yield AllianceStatus.RECEIVED;
              }
              yield result;
            }
            case SENT -> {
              if (alliance.ally() == coally && alliance.coally() == ally) {
                yield AllianceStatus.COMPLETE;
              }
              yield result;
            }
            case RECEIVED -> {
              if (alliance.ally() == ally && alliance.coally() == coally) {
                yield AllianceStatus.COMPLETE;
              }
              yield result;
            }
            case COMPLETE -> result;
          };
        }

        yield result;
      }
    };
  }

}
