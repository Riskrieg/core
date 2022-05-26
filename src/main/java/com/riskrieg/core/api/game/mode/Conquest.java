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
import com.riskrieg.core.api.game.Game;
import com.riskrieg.core.api.game.GameConstants;
import com.riskrieg.core.api.game.GamePhase;
import com.riskrieg.core.api.game.Save;
import com.riskrieg.core.api.game.entity.nation.Nation;
import com.riskrieg.core.api.game.entity.player.Player;
import com.riskrieg.core.api.game.map.GameMap;
import com.riskrieg.core.api.game.order.TurnOrder;
import com.riskrieg.core.api.game.territory.Claim;
import com.riskrieg.core.api.game.territory.GameTerritory;
import com.riskrieg.core.api.game.territory.TerritoryType;
import com.riskrieg.core.api.identifier.GameIdentifier;
import com.riskrieg.core.api.identifier.NationIdentifier;
import com.riskrieg.core.api.identifier.PlayerIdentifier;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class Conquest implements Game {

  // Immutable
  private final GameIdentifier identifier;
  private final GameConstants constants;
  private final Instant creationTime;
  private final Set<Nation> nations;
  private final Set<Claim> claims;

  // Mutable
  private ColorPalette colors;
  private Instant updatedTime;

  private Deque<Player> players;

  private GamePhase phase;
  private GameMap map; // Nullable

  public Conquest(Save save, Path mapRepository) {
    if (save.constants().maximumPlayers() != save.colors().size()) {
      throw new IllegalStateException("The maximum number of players must equal the amount of colors provided");
    }
    this.identifier = save.identifier();
    this.constants = save.constants();
    this.colors = save.colors();
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

  public Conquest(GameIdentifier identifier, GameConstants constants, ColorPalette colors) {
    if (constants.maximumPlayers() != colors.size()) {
      throw new IllegalStateException("The maximum number of players must equal the amount of colors provided");
    }
    this.identifier = identifier;
    this.constants = constants;
    this.colors = colors;
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
  public ColorPalette colors() {
    return colors;
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
  public GameAction<GameMap> selectMap(GameMap map) {
    this.updatedTime = Instant.now();
    try {
      return switch (phase) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case RUNNING -> throw new IllegalStateException("The map can only be set during the setup phase");
        case SETUP -> {
          Objects.requireNonNull(map);
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
    this.updatedTime = Instant.now();
    try {
      return switch (phase) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case RUNNING -> throw new IllegalStateException("Players can only be added during the setup phase");
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
    this.updatedTime = Instant.now();
    try {
      return switch (phase) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case SETUP, RUNNING -> {
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
    this.updatedTime = Instant.now();
    try {
      return switch (phase) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case RUNNING -> throw new IllegalStateException("Nations can only be created in the setup phase");
        case SETUP -> {
          Nation nation = new Nation(NationIdentifier.uuid(), color.id(), identifier);
          if (players.stream().noneMatch(p -> p.identifier().equals(identifier))) {
            throw new IllegalStateException("A player must join the game before creating a nation");
          }
          if (nations.size() >= colors.size()) {
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
  public GameAction<Boolean> claim(Attack attack, NationIdentifier identifier, GameTerritory territory, GameTerritory... territories) {
    this.updatedTime = Instant.now();
    try {
      return switch (phase) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case RUNNING -> { // claim
          var nation = getNation(identifier);
          if (nation.isEmpty()) {
            throw new IllegalStateException("That nation does not exist");
          }
          if (!players.getFirst().identifier().equals(nation.get().leaderIdentifier())) {
            throw new IllegalStateException("It is not that player's turn");
          }
          if (map == null) {
            throw new IllegalStateException("A valid map must be selected before claiming territories");
          }
          // TODO: Implement new claim functionality
          Set<GameTerritory> territoriesToClaim = new HashSet<>(Set.of(territories));
          territoriesToClaim.add(territory);
          long allowedClaimsPerTurn = GameUtil.getAllowedClaimsPerTurn(identifier, claims, constants, map);
          if (territoriesToClaim.size() != allowedClaimsPerTurn) {
            throw new IllegalStateException(
                "You must claim exactly " + allowedClaimsPerTurn + " territories, but are trying to claim " + territoriesToClaim.size() + " territories.");
          }

          yield new GenericAction<>(false); // TODO: Implement
        }
        case SETUP -> {
          if (territories.length > 0) {
            throw new IllegalStateException("Only one territory can be claimed during the setup phase");
          }
          if (nations.stream().noneMatch(nation -> nation.identifier().equals(identifier))) {
            throw new IllegalStateException("That nation does not exist");
          }
          if (map == null) {
            throw new IllegalStateException("A valid map must be selected before claiming territories");
          }
          if (GameUtil.territoryNotExists(territory.identifier(), map)) {
            throw new IllegalStateException("That territory does not exist on the current map");
          }
          if (!territory.type().equals(TerritoryType.CAPITAL)) {
            throw new IllegalStateException("The territory type provided must be a capital during the setup phase");
          }
          if (GameUtil.territoryIsClaimed(territory.identifier(), claims)) {
            throw new IllegalStateException("That territory is already claimed by someone else");
          }
          if (GameUtil.nationClaimsAnyTerritory(identifier, claims, TerritoryType.CAPITAL)) {
            claims.removeIf(claim -> claim.identifier().equals(identifier) && claim.territory().type().equals(TerritoryType.CAPITAL));
          }
          claims.add(new Claim(identifier, territory));
          yield new GenericAction<>(true);
        }
      };
    } catch (Exception e) {
      return new GenericAction<>(false, e);
    }
  }

  @NonNull
  @Override
  public GameAction<Boolean> unclaim(NationIdentifier identifier, GameTerritory territory, GameTerritory... territories) {
    this.updatedTime = Instant.now(); // TODO: Implement
    return null;
  }

  @NonNull
  @Override
  public GameAction<Player> start(TurnOrder order) {
    this.updatedTime = Instant.now();
    try {
      return switch (phase) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case RUNNING -> throw new IllegalStateException("A game can only be started in the setup phase");
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
          if (!nations.stream().allMatch(nation -> GameUtil.getTerritorialClaimCount(nation.identifier(), claims) == 1)) {
            throw new IllegalStateException("All nations must claim exactly one territory.");
          }
          this.players = order.getSorted(players, nations);
          this.phase = GamePhase.RUNNING;
          yield new GenericAction<>(players.getFirst());
        }
      };
    } catch (Exception e) {
      return new GenericAction<>(e);
    }
  }

  @NonNull
  @Override
  public GameAction<Boolean> advanceTurn() { // TODO: Unimplemented
    this.updatedTime = Instant.now();
    players.addLast(players.removeFirst());
    return new GenericAction<>(false);
  }

}
