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

import com.riskrieg.core.api.color.ColorPalette;
import com.riskrieg.core.api.color.GameColor;
import com.riskrieg.core.api.game.Attack;
import com.riskrieg.core.api.game.ClaimOverride;
import com.riskrieg.core.api.game.EndReason;
import com.riskrieg.core.api.game.Game;
import com.riskrieg.core.api.game.GameConstants;
import com.riskrieg.core.api.game.GamePhase;
import com.riskrieg.core.api.game.Save;
import com.riskrieg.core.api.game.entity.nation.Nation;
import com.riskrieg.core.api.game.entity.player.Player;
import com.riskrieg.core.api.game.event.ClaimEvent;
import com.riskrieg.core.api.game.event.TurnAdvanceEvent;
import com.riskrieg.core.api.game.map.GameMap;
import com.riskrieg.core.api.game.order.TurnOrder;
import com.riskrieg.core.api.game.territory.Claim;
import com.riskrieg.core.api.game.territory.GameTerritory;
import com.riskrieg.core.api.game.territory.TerritoryType;
import com.riskrieg.core.api.identifier.GameIdentifier;
import com.riskrieg.core.api.identifier.NationIdentifier;
import com.riskrieg.core.api.identifier.PlayerIdentifier;
import com.riskrieg.core.api.identifier.TerritoryIdentifier;
import com.riskrieg.core.api.requests.GameAction;
import com.riskrieg.core.decode.RkmDecoder;
import com.riskrieg.core.internal.requests.GenericAction;
import com.riskrieg.core.util.game.GameUtil;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
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

  // Mutable
  private ColorPalette palette;
  private Instant updatedTime;

  private Deque<Player> players;

  private GamePhase phase;
  private GameMap map; // Nullable

  public Conquest(Save save, Path mapRepository) {
    if (save.palette().size() != save.constants().maximumPlayers()) {
      throw new IllegalStateException("The maximum number of players must equal the amount of colors provided");
    }
    this.identifier = save.identifier();
    this.constants = save.constants();
    this.palette = save.palette();
    this.creationTime = save.creationTime();
    this.updatedTime = save.updatedTime();
    this.phase = save.phase();
    if (!save.mapCodename().isBlank()) {
      RkmDecoder decoder = new RkmDecoder();
      try {
        decoder.decode(mapRepository.resolve(save.mapCodename() + ".rkm"));
      } catch (IOException | NoSuchAlgorithmException e) {
        throw new RuntimeException(e); // Panic, map can't be loaded.
      }
    }
    this.players = new ArrayDeque<>(save.players());
    this.nations = save.nations();
    this.claims = save.claims();
  }

  public Conquest(GameIdentifier identifier, GameConstants constants, ColorPalette palette) {
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
  public ColorPalette palette() {
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
  public GameMap map() {
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
  public GameAction<Boolean> setPalette(ColorPalette palette) {
    Objects.requireNonNull(palette);
    try {
      return switch (phase) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case SETUP, ACTIVE -> {
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
  public GameAction<GameMap> selectMap(GameMap map) {
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
              .forEach(nation -> claims.removeIf(claim -> claim.identifier().equals(nation.identifier())));
          nations.removeIf(nation -> nation.leaderIdentifier().equals(identifier));
          players.removeIf(player -> player.identifier().equals(identifier));

          // TODO: Potentially end game when appropriate, maybe do in advanceTurn

          yield new GenericAction<>(true);
        }
      };
    } catch (Exception e) {
      return new GenericAction<>(false, e);
    }
  }

  @NonNull
  @Override
  public GameAction<Nation> createNation(GameColor color, PlayerIdentifier identifier) {
    Objects.requireNonNull(color);
    Objects.requireNonNull(identifier);
    this.updatedTime = Instant.now();
    try {
      return switch (phase) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case ACTIVE -> throw new IllegalStateException("Nations can only be created in the setup phase");
        case SETUP -> {
          Nation nation = new Nation(NationIdentifier.uuid(), color.id(), identifier);
          if (players.stream().noneMatch(p -> p.identifier().equals(identifier))) {
            throw new IllegalStateException("A player must join the game before creating a nation");
          }
          if (nations.size() >= palette.size()) {
            throw new IllegalStateException("The nation could not be formed because the maximum number of nations has already been reached");
          }
          if (nations.stream().anyMatch(n -> n.leaderIdentifier().equals(identifier))) {
            throw new IllegalStateException("That player is already in another nation");
          }
          if (nations.stream().anyMatch(n -> n.colorId() == color.id())) {
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
  public GameAction<Boolean> dissolveNation(GameColor color) {
    Objects.requireNonNull(color);
    return new GenericAction<>(false, new IllegalAccessException("The dissolve nation action is unavailable in this game mode."));
  }

  @NonNull
  @Override
  public GameAction<ClaimEvent> claim(Attack attack, NationIdentifier identifier, ClaimOverride override, TerritoryIdentifier territory, TerritoryIdentifier... territories) {
    Objects.requireNonNull(attack);
    Objects.requireNonNull(identifier);
    Objects.requireNonNull(territory);
    Objects.requireNonNull(territories);
    this.updatedTime = Instant.now();
    try {
      return switch (phase) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case ACTIVE -> {
          if (map == null) {
            throw new IllegalStateException("A valid map must be selected before claiming territories");
          }
          Optional<Nation> nation = getNation(identifier);
          if (nation.isEmpty()) {
            throw new IllegalStateException("That nation does not exist");
          }
          Nation attacker = nation.get();
          if (!players.getFirst().identifier().equals(attacker.leaderIdentifier())) {
            throw new IllegalStateException("It is not that player's turn");
          }

          Set<TerritoryIdentifier> territoriesToClaim = new HashSet<>(Set.of(territories));
          territoriesToClaim.add(territory);
          var invalidTerritories = territoriesToClaim.stream().filter(territoryIdentifier -> GameUtil.territoryNotExists(territoryIdentifier, map)).toList();
          if (!invalidTerritories.isEmpty()) {
            throw new IllegalStateException("The following territories do not exist or are otherwise invalid: "
                + invalidTerritories.stream().map(TerritoryIdentifier::id).collect(Collectors.joining(", ")).trim());
          }
          var alreadyClaimedTerritories = territoriesToClaim.stream().filter(territoryIdentifier -> attacker.hasClaimOn(territoryIdentifier, claims)).toList();
          if (!alreadyClaimedTerritories.isEmpty()) {
            throw new IllegalStateException("The following territories are already claimed by you: "
                + alreadyClaimedTerritories.stream().map(TerritoryIdentifier::id).collect(Collectors.joining(", ")).trim());
          }
          var notNeighboringTerritories = territoriesToClaim.stream().filter(territoryIdentifier -> attacker.isTerritoryNeighboring(territoryIdentifier, claims, map)).toList();
          if (!notNeighboringTerritories.isEmpty()) {
            throw new IllegalStateException("The following territories are not neighboring your nation: "
                + notNeighboringTerritories.stream().map(TerritoryIdentifier::id).collect(Collectors.joining(", ")).trim());
          }

          long allowedClaimAmount = attacker.getAllowedClaimAmount(claims, constants, map);
          if (allowedClaimAmount == 0) {
            throw new IllegalStateException("Trying to claim " + territoriesToClaim.size() + (territoriesToClaim.size() == 1 ? " territory" : " territories")
                + " but unable to claim any territories.");
          }

          switch (override) {
            case NONE -> {
              if (allowedClaimAmount != territoriesToClaim.size()) { // If there's no override, must match exactly.
                throw new IllegalStateException("Trying to claim " + territoriesToClaim.size() + (territoriesToClaim.size() == 1 ? " territory" : " territories")
                    + " but must claim " + allowedClaimAmount + (allowedClaimAmount == 1 ? " territory" : " territories"));
              }
            }
            case AUTO -> {
              if (allowedClaimAmount > territoriesToClaim.size()) { // Invalid state
                throw new IllegalStateException("Trying to claim " + territoriesToClaim.size() + (territoriesToClaim.size() == 1 ? " territory" : " territories")
                    + " but cannot claim more than " + allowedClaimAmount + (allowedClaimAmount == 1 ? " territory" : " territories"));
              } else if (allowedClaimAmount < territoriesToClaim.size()) { // Add random territories to fill out the rest
                var claimable = new ArrayList<>(attacker.getClaimableTerritories(claims, map));
                claimable.removeAll(territoriesToClaim);
                Collections.shuffle(claimable);
                claimable.stream()
                    .limit(allowedClaimAmount - territoriesToClaim.size())
                    .forEach(territoriesToClaim::add);

                if (allowedClaimAmount != territoriesToClaim.size()) { // Check to make sure it matches exactly, just for sanity
                  throw new IllegalStateException("Auto: Trying to claim " + territoriesToClaim.size() + (territoriesToClaim.size() == 1 ? " territory" : " territories")
                      + " but must claim " + allowedClaimAmount + (allowedClaimAmount == 1 ? " territory" : " territories"));
                }
              }
            }
            case EXACT -> {
              if (allowedClaimAmount > territoriesToClaim.size()) { // Invalid state
                throw new IllegalStateException("Trying to claim " + territoriesToClaim.size() + (territoriesToClaim.size() == 1 ? " territory" : " territories")
                    + " but cannot claim more than " + allowedClaimAmount + (allowedClaimAmount == 1 ? " territory" : " territories"));
              }
            }
          }

          Set<Claim> freeClaims = new HashSet<>();
          Set<Claim> wonClaims = new HashSet<>();
          Set<Claim> defendedClaims = new HashSet<>();

          for (TerritoryIdentifier attackedTerritory : territoriesToClaim) {
            Optional<Nation> defendingNation = getNation(attackedTerritory);
            if (defendingNation.isPresent()) {
              Nation defender = defendingNation.get();
              if (attack.success(attacker, defender, attackedTerritory, map, claims, constants)) { // Attacker wins, so the claim is transferred to the attacker
                boolean wasCapital = defender.hasClaimOn(attackedTerritory, claims, TerritoryType.CAPITAL);
                claims.removeIf(claim -> claim.identifier().equals(defender.identifier()) && claim.territory().identifier().equals(attackedTerritory));
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

          yield new GenericAction<>(new ClaimEvent(freeClaims, wonClaims, defendedClaims));
        }
        case SETUP -> {
          if (map == null) {
            throw new IllegalStateException("A valid map must be selected before claiming territories");
          }
          if (territories.length > 0) {
            throw new IllegalStateException("Only one territory can be claimed during the setup phase");
          }
          Optional<Nation> nation = getNation(identifier);
          if (nation.isEmpty()) {
            throw new IllegalStateException("That nation does not exist");
          }
          if (GameUtil.territoryNotExists(territory, map)) {
            throw new IllegalStateException("That territory does not exist on the current map");
          }
          if (GameUtil.territoryIsClaimed(territory, claims)) {
            throw new IllegalStateException("That territory is already claimed by someone else");
          }
          if (nation.get().hasAnyClaim(claims, TerritoryType.CAPITAL)) {
            claims.removeIf(claim -> claim.identifier().equals(identifier) && claim.territory().type().equals(TerritoryType.CAPITAL));
          }
          var freeClaim = new Claim(identifier, new GameTerritory(territory, TerritoryType.CAPITAL));
          claims.add(freeClaim);
          yield new GenericAction<>(new ClaimEvent(Set.of(freeClaim)));
        }
      };
    } catch (Exception e) {
      return new GenericAction<>(e);
    }
  }

  @NonNull
  @Override
  public GameAction<Boolean> unclaim(NationIdentifier identifier, TerritoryIdentifier territory, TerritoryIdentifier... territories) {
    Objects.requireNonNull(identifier);
    Objects.requireNonNull(territory);
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
          Set<TerritoryIdentifier> territoriesToUnclaim = new HashSet<>(Set.of(territories));
          territoriesToUnclaim.add(territory);
          var invalidTerritories = territoriesToUnclaim.stream().filter(territoryIdentifier -> GameUtil.territoryNotExists(territoryIdentifier, map)).toList();
          if (!invalidTerritories.isEmpty()) {
            throw new IllegalStateException("The following territories do not exist or are otherwise invalid: "
                + invalidTerritories.stream().map(TerritoryIdentifier::id).collect(Collectors.joining(", ")).trim());
          }
          var notClaimedTerritories = territoriesToUnclaim.stream().filter(territoryIdentifier -> !nation.hasClaimOn(territoryIdentifier, claims)).toList();
          if (!notClaimedTerritories.isEmpty()) {
            throw new IllegalStateException("The following territories are not claimed by you: "
                + notClaimedTerritories.stream().map(TerritoryIdentifier::id).collect(Collectors.joining(", ")).trim());
          }
          for (TerritoryIdentifier territoryIdentifier : territoriesToUnclaim) {
            claims.removeIf(claim -> claim.territory().identifier().equals(territoryIdentifier));
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
          Set<TerritoryIdentifier> territoriesToUnclaim = new HashSet<>(Set.of(territories));
          territoriesToUnclaim.add(territory);
          var invalidTerritories = territoriesToUnclaim.stream().filter(territoryIdentifier -> GameUtil.territoryNotExists(territoryIdentifier, map)).toList();
          if (!invalidTerritories.isEmpty()) {
            throw new IllegalStateException("The following territories do not exist or are otherwise invalid: "
                + invalidTerritories.stream().map(TerritoryIdentifier::id).collect(Collectors.joining(", ")).trim());
          }
          var notClaimedTerritories = territoriesToUnclaim.stream().filter(territoryIdentifier -> !nation.hasClaimOn(territoryIdentifier, claims)).toList();
          if (!notClaimedTerritories.isEmpty()) {
            throw new IllegalStateException("The following territories are not claimed by you: "
                + notClaimedTerritories.stream().map(TerritoryIdentifier::id).collect(Collectors.joining(", ")).trim());
          }
          for (TerritoryIdentifier territoryIdentifier : territoriesToUnclaim) {
            claims.removeIf(claim -> claim.territory().identifier().equals(territoryIdentifier));
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
  public GameAction<TurnAdvanceEvent> advanceTurn() {
    this.updatedTime = Instant.now();
    try {
      return switch (phase) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case ACTIVE -> {
          Player previous = players.getFirst();
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
          } else if (nations.stream().allMatch(nation -> nation.getAllowedClaimAmount(claims, constants, map) == 0)) {
            endReason = EndReason.STALEMATE;
            phase = GamePhase.ENDED;
          }

          players.addLast(players.removeFirst());
          yield new GenericAction<>(new TurnAdvanceEvent(players.getFirst(), previous, defeated, endReason));
        }
        case SETUP -> throw new IllegalStateException("Turns can only be advanced while the game is in the active phase");
      };
    } catch (Exception e) {
      return new GenericAction<>(e);
    }
  }

}
