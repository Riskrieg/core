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

import com.riskrieg.core.api.color.ColorBatch;
import com.riskrieg.core.api.color.GameColor;
import com.riskrieg.core.api.game.Game;
import com.riskrieg.core.api.game.GameConstants;
import com.riskrieg.core.api.game.GameState;
import com.riskrieg.core.api.game.Save;
import com.riskrieg.core.api.game.entity.nation.Nation;
import com.riskrieg.core.api.game.entity.player.Player;
import com.riskrieg.core.api.game.map.GameMap;
import com.riskrieg.core.api.game.order.TurnOrder;
import com.riskrieg.core.api.game.territory.GameTerritory;
import com.riskrieg.core.api.identifier.GameIdentifier;
import com.riskrieg.core.api.identifier.NationIdentifier;
import com.riskrieg.core.api.identifier.PlayerIdentifier;
import com.riskrieg.core.api.identifier.TerritoryIdentifier;
import com.riskrieg.core.api.requests.GameAction;
import com.riskrieg.core.decode.RkmDecoder;
import com.riskrieg.core.internal.requests.GenericAction;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayDeque;
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
  private final Set<GameTerritory> territories;

  // Mutable
  private ColorBatch colors;
  private Instant updatedTime;

  private Deque<Player> players;

  private GameState state;
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
    this.state = save.state();
    if (!save.mapCodename().isBlank()) {
      RkmDecoder decoder = new RkmDecoder();
      try {
        decoder.decode(mapRepository.resolve(save.mapCodename() + ".rkm"));
      } catch (IOException | NoSuchAlgorithmException e) {
        throw new RuntimeException(e); // Panic, map can't be loaded.
      }
    }
    this.players = save.players();
    this.nations = save.nations();
    this.territories = save.territories();
  }

  public Conquest(GameIdentifier identifier, GameConstants constants, ColorBatch colors) {
    if (constants.maximumPlayers() != colors.size()) {
      throw new IllegalStateException("The maximum number of players must equal the amount of colors provided");
    }
    this.identifier = identifier;
    this.constants = constants;
    this.colors = colors;
    this.creationTime = Instant.now();
    this.updatedTime = Instant.now();
    this.state = GameState.SETUP;
    this.players = new ArrayDeque<>();
    this.nations = new HashSet<>();
    this.territories = new HashSet<>();
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
  public ColorBatch colors() {
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
  public GameState state() {
    return state;
  }

  @NonNull
  @Override
  public Deque<Player> players() {
    return players; // TODO: Come back to this, should be unmodifiable
  }

  @NonNull
  @Override
  public Set<Nation> nations() {
    return Collections.unmodifiableSet(nations);
  }

  @NonNull
  @Override
  public Set<GameTerritory> territories() {
    return Collections.unmodifiableSet(territories);
  }

  @Override
  public GameMap map() {
    return null;
  }

  @NonNull
  @Override
  public GameAction<GameMap> selectMap(GameMap map) {
    this.updatedTime = Instant.now();
    try {
      return switch (state) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case RUNNING -> throw new IllegalStateException("The map can only be set during the setup phase");
        case SETUP -> {
          Objects.requireNonNull(map);
          this.map = map;
          // TODO: Clear territories
          nations.clear();
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
      return switch (state) {
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
  public GameAction<?> removePlayer(PlayerIdentifier identifier) {
    this.updatedTime = Instant.now();
    return null;
  }

  @NonNull
  @Override
  public GameAction<Nation> createNation(GameColor color, PlayerIdentifier identifier) {
    this.updatedTime = Instant.now();
    try {
      return switch (state) {
        case ENDED -> throw new IllegalStateException("A new game must be created in order to do that");
        case RUNNING -> throw new IllegalStateException("Nations can only be created in the setup phase");
        case SETUP -> {
          Nation nation = new Nation(NationIdentifier.uuid(), color.id(), identifier);
          if (players.stream().noneMatch(p -> p.id().equals(identifier))) {
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
          yield new GenericAction<>(nation);
        }
      };
    } catch (Exception e) {
      return new GenericAction<>(e);
    }
  }

  @NonNull
  @Override
  public GameAction<?> addTerritory(NationIdentifier nation, TerritoryIdentifier territory, TerritoryIdentifier... territories) {
    this.updatedTime = Instant.now();
    return null;
  }

  @NonNull
  @Override
  public GameAction<?> removeTerritory(NationIdentifier nation, TerritoryIdentifier territory, TerritoryIdentifier... territories) {
    this.updatedTime = Instant.now();
    return null;
  }

  @NonNull
  @Override
  public GameAction<Player> start(TurnOrder order) {
    this.updatedTime = Instant.now();
    try {
      return switch (state) {
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
          // TODO: Check to make sure all nations have 1 territory
          this.players = order.getSorted(players, nations);
          this.state = GameState.RUNNING;
          yield new GenericAction<>(players.getFirst());
        }
      };
    } catch (Exception e) {
      return new GenericAction<>(e);
    }
  }

  @NonNull
  @Override
  public GameAction<?> skip(PlayerIdentifier identifier) {
    this.updatedTime = Instant.now();
    return null;
  }

  @NonNull
  @Override
  public GameAction<?> claim() {
    this.updatedTime = Instant.now();
    return null;
  }

}
