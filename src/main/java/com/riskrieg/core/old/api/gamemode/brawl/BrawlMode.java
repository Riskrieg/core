/*
 *     Riskrieg, an open-source conflict simulation game.
 *     Copyright (C) 2021  Aaron Yoder <aaronjyoder@gmail.com>
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

package com.riskrieg.core.old.api.gamemode.brawl;

import com.riskrieg.core.old.api.Save;
import com.riskrieg.core.old.api.gamemode.AlliableMode;
import com.riskrieg.core.old.api.gamemode.GameID;
import com.riskrieg.core.old.api.gamemode.GameState;
import com.riskrieg.core.old.api.map.GameMap;
import com.riskrieg.core.old.api.map.MapOptions;
import com.riskrieg.core.old.api.map.TerritoryType;
import com.riskrieg.core.old.api.nation.Nation;
import com.riskrieg.core.old.api.order.TurnOrder;
import com.riskrieg.core.old.api.player.Identity;
import com.riskrieg.core.old.api.player.Player;
import com.riskrieg.core.old.constant.color.ColorId;
import com.riskrieg.core.old.internal.action.Action;
import com.riskrieg.core.old.internal.action.LeaveAction;
import com.riskrieg.core.old.internal.action.running.AllyAction;
import com.riskrieg.core.old.internal.action.running.SelectTerritoryAction;
import com.riskrieg.core.old.internal.action.running.SkipAction;
import com.riskrieg.core.old.internal.action.running.UnallyAction;
import com.riskrieg.core.old.internal.action.running.claim.BrawlClaimAction;
import com.riskrieg.core.old.internal.action.running.update.BrawlUpdateAction;
import com.riskrieg.core.old.internal.action.setup.JoinAction;
import com.riskrieg.core.old.internal.action.setup.SelectMapAction;
import com.riskrieg.core.old.internal.action.setup.start.BrawlStartAction;
import com.riskrieg.core.old.internal.bundle.AllianceBundle;
import com.riskrieg.core.old.internal.bundle.ClaimBundle;
import com.riskrieg.core.old.internal.bundle.CurrentStateBundle;
import com.riskrieg.core.old.internal.bundle.LeaveBundle;
import com.riskrieg.core.old.internal.bundle.SkipBundle;
import com.riskrieg.core.old.internal.bundle.UpdateBundle;
import com.riskrieg.map.RkmMap;
import com.riskrieg.map.territory.TerritoryId;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public final class BrawlMode implements AlliableMode {

  private final GameID id;
  private final Instant creationTime;
  private Instant lastUpdated;

  private GameState gameState;
  private GameMap gameMap;

  private Deque<Player> players;
  private Set<Nation> nations;

  public BrawlMode() {
    this.id = GameID.random();
    this.creationTime = Instant.now();
    this.lastUpdated = Instant.now();
    this.gameState = GameState.SETUP;
    this.gameMap = new GameMap();

    this.players = new ArrayDeque<>();
    this.nations = new HashSet<>();
  }

  public BrawlMode(Save save) {
    this.id = save.id();
    this.creationTime = save.creationTime().asInstant();
    this.lastUpdated = save.lastUpdated().asInstant();
    this.gameState = save.gameState();
    if (save.mapSimpleName() == null) {
      this.gameMap = new GameMap();
    } else {
      // TODO: Temporary
      var optMap = RkmMap.load(Path.of("res/maps/" + save.mapSimpleName() + ".rkm"));
      var optOptions = MapOptions.load(Path.of("res/maps/options/" + save.mapSimpleName() + ".json"), false);
      if (optMap.isPresent() && optOptions.isPresent()) {
        this.gameMap = new GameMap(optMap.get(), optOptions.get());
      } else {
        this.gameMap = new GameMap();
      }
    }
    this.players = new ArrayDeque<>(save.players());
    this.nations = new HashSet<>(save.nations());
  }

  @NonNull
  @Override
  public String displayName() {
    return "Brawl";
  }

  @NonNull
  @Override
  public GameID id() {
    return id;
  }

  @NonNull
  @Override
  public Instant creationTime() {
    return creationTime;
  }

  @NonNull
  @Override
  public Instant lastUpdated() {
    return lastUpdated;
  }

  @NonNull
  @Override
  public GameState gameState() {
    return gameState;
  }

  @Override
  public void setGameState(@NonNull GameState gameState) {
    this.gameState = gameState;
  }

  @Override
  public boolean isEnded() {
    return gameState.equals(GameState.ENDED);
  }

  @NonNull
  @Override
  public Collection<Player> players() {
    return Collections.unmodifiableCollection(players);
  }

  @NonNull
  @Override
  public Collection<Nation> nations() {
    return Collections.unmodifiableCollection(nations);
  }

  @NonNull
  @Override
  public GameMap map() {
    return gameMap;
  } // TODO: Return unmodifiable version of GameMap

  /* Setup */

  @NonNull
  @Override
  public Action<Player> join(@NonNull Identity identity, @NonNull String name, @NonNull ColorId colorId) {
    setLastUpdated();
    return new JoinAction(identity, name, colorId, gameState, players);
  }

  @NonNull
  @Override
  public Action<LeaveBundle> leave(@NonNull Identity identity) {
    setLastUpdated();
    return new LeaveAction(identity, this, players, nations);
  }

  @NonNull
  @Override
  public Action<GameMap> selectMap(@NonNull RkmMap rkmMap, @NonNull MapOptions options) {
    setLastUpdated();
    return new SelectMapAction(rkmMap, options, gameState, gameMap, nations);
  }

  @NonNull
  @Override
  public Action<Player> start(@NonNull TurnOrder order) {
    if (gameState.equals(GameState.SETUP)) {
      setLastUpdated();
      players = order.sort(players);
    }
    return new BrawlStartAction(this, gameMap, players, nations);
  }

  /* Selection */

  @NonNull
  @Override
  public Action<Nation> selectTerritory(@NonNull Identity identity, @NonNull TerritoryId territoryId) {
    setLastUpdated();
    return new SelectTerritoryAction(identity, territoryId, TerritoryType.NORMAL, gameState, gameMap, players, nations);
  }

  /* Running */

  @NonNull
  @Override
  public Action<SkipBundle> skip(Identity identity) {
    setLastUpdated();
    return new SkipAction(identity, gameState, gameMap, players, nations);
  }

  @NonNull
  @Override
  public Action<SkipBundle> skipSelf(Identity identity) {
    setLastUpdated();
    return new SkipAction(identity, gameState, gameMap, players, nations, true);
  }

  @NonNull
  @Override
  public Action<ClaimBundle> claim(Identity identity, TerritoryId... territoryIds) {
    setLastUpdated();
    return new BrawlClaimAction(identity, Set.of(territoryIds), players.getFirst().identity(), gameState, gameMap, nations);
  }

  @NonNull
  @Override
  public Action<UpdateBundle> update() {
    setLastUpdated();
    return new BrawlUpdateAction(this, gameState, gameMap, players, nations);
  }

  @NonNull
  @Override
  public CurrentStateBundle currentTurn() {
    Player currentTurnPlayer = players.size() > 0 ? players.getFirst() : null;
    Nation nation = currentTurnPlayer == null ? null : nations.stream().filter(n -> n.identity().equals(currentTurnPlayer.identity())).findAny().orElse(null);
    int claims;
    if (gameState().equals(GameState.SELECTION)) {
      claims = 1;
    } else {
      claims = nation == null ? -1 : nation.getClaimAmount(map(), nations);
    }
    return new CurrentStateBundle(players.getFirst(), gameState, claims);
  }

  @Override
  public boolean allied(Identity identity1, Identity identity2) {
    Optional<Nation> nation1 = nations.stream().filter(nation -> nation.identity().equals(identity1)).findAny();
    Optional<Nation> nation2 = nations.stream().filter(nation -> nation.identity().equals(identity2)).findAny();
    if (nation1.isPresent() && nation2.isPresent()) {
      return nation1.get().isAllied(identity2) && nation2.get().isAllied(identity1);
    }
    return false;
  }

  @NonNull
  @Override
  public Action<AllianceBundle> ally(Identity identity1, Identity identity2) {
    setLastUpdated();
    return new AllyAction(identity1, identity2, this, gameState, players, nations);
  }

  @NonNull
  @Override
  public Action<AllianceBundle> unally(Identity identity1, Identity identity2) {
    setLastUpdated();
    return new UnallyAction(identity1, identity2, gameState, players, nations);
  }

  /* Private Methods */

  private void setLastUpdated() {
    this.lastUpdated = Instant.now();
  }

}
