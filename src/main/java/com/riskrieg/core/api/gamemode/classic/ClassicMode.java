package com.riskrieg.core.api.gamemode.classic;

import com.riskrieg.core.api.Save;
import com.riskrieg.core.api.gamemode.GameID;
import com.riskrieg.core.api.gamemode.GameMode;
import com.riskrieg.core.api.gamemode.GameState;
import com.riskrieg.core.api.map.GameMap;
import com.riskrieg.core.api.map.MapOptions;
import com.riskrieg.core.api.map.TerritoryType;
import com.riskrieg.core.api.nation.Nation;
import com.riskrieg.core.api.order.TurnOrder;
import com.riskrieg.core.api.player.Identity;
import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.constant.color.ColorId;
import com.riskrieg.core.internal.action.Action;
import com.riskrieg.core.internal.action.LeaveAction;
import com.riskrieg.core.internal.action.running.SkipAction;
import com.riskrieg.core.internal.action.running.claim.SimpleClaimAction;
import com.riskrieg.core.internal.action.running.update.SimpleUpdateAction;
import com.riskrieg.core.internal.action.setup.FormNationAction;
import com.riskrieg.core.internal.action.setup.JoinAction;
import com.riskrieg.core.internal.action.setup.SelectMapAction;
import com.riskrieg.core.internal.action.setup.start.StartAction;
import com.riskrieg.core.internal.bundle.ClaimBundle;
import com.riskrieg.core.internal.bundle.CurrentStateBundle;
import com.riskrieg.core.internal.bundle.LeaveBundle;
import com.riskrieg.core.internal.bundle.SkipBundle;
import com.riskrieg.core.internal.bundle.UpdateBundle;
import com.riskrieg.map.RkmMap;
import com.riskrieg.map.territory.TerritoryId;
import java.awt.Color;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nonnull;

public final class ClassicMode implements GameMode {

  private final GameID id;
  private final Instant creationTime;
  private Instant lastUpdated;

  private GameState gameState;
  private GameMap gameMap;

  private Deque<Player> players;
  private Set<Nation> nations;

  public ClassicMode() {
    this.id = GameID.random();
    this.creationTime = Instant.now();
    this.lastUpdated = Instant.now();
    this.gameState = GameState.SETUP;
    this.gameMap = new GameMap();

    this.players = new ArrayDeque<>();
    this.nations = new HashSet<>();
  }

  public ClassicMode(Save save) {
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

  @Nonnull
  @Override
  public String displayName() {
    return "Classic";
  }

  @Nonnull
  @Override
  public GameID id() {
    return id;
  }

  @Nonnull
  @Override
  public Instant creationTime() {
    return creationTime;
  }

  @Nonnull
  @Override
  public Instant lastUpdated() {
    return lastUpdated;
  }

  @Nonnull
  @Override
  public GameState gameState() {
    return gameState;
  }

  @Override
  public void setGameState(@Nonnull GameState gameState) {
    this.gameState = gameState;
  }

  @Override
  public boolean isEnded() {
    return gameState.equals(GameState.ENDED);
  }

  @Nonnull
  @Override
  public Collection<Player> players() {
    return Collections.unmodifiableCollection(players);
  }

  @Nonnull
  @Override
  public Collection<Nation> nations() {
    return Collections.unmodifiableCollection(nations);
  }

  @Nonnull
  @Override
  public GameMap map() {
    return gameMap;
  } // TODO: Return unmodifiable version of GameMap

  /* Setup */

  @Nonnull
  @Override
  @Deprecated
  public Action<Player> join(@Nonnull Identity identity, @Nonnull String name, @Nonnull Color color) {
    setLastUpdated();
    return new JoinAction(identity, name, color, gameState, players);
  }

  @Nonnull
  @Override
  public Action<Player> join(@Nonnull Identity identity, @Nonnull String name, @Nonnull ColorId colorId) {
    setLastUpdated();
    return new JoinAction(identity, name, colorId, gameState, players);
  }

  @Nonnull
  @Override
  public Action<LeaveBundle> leave(@Nonnull Identity identity) {
    setLastUpdated();
    return new LeaveAction(identity, this, players, nations);
  }

  @Nonnull
  @Override
  public Action<GameMap> selectMap(@Nonnull RkmMap rkmMap, @Nonnull MapOptions options) {
    setLastUpdated();
    return new SelectMapAction(rkmMap, options, gameState, gameMap, nations);
  }

  @Nonnull
  @Override
  public Action<Nation> selectTerritory(@Nonnull Identity identity, @Nonnull TerritoryId territoryId) {
    setLastUpdated();
    return new FormNationAction(identity, territoryId, TerritoryType.NORMAL, gameState, gameMap, players, nations);
  }

  @Nonnull
  @Override
  public Action<Player> start(@Nonnull TurnOrder order) {
    if (gameState.equals(GameState.SETUP)) {
      setLastUpdated();
      players = order.sort(players);
    }
    return new StartAction(this, gameMap, players, nations);
  }

  /* Running */

  @Nonnull
  @Override
  public Action<SkipBundle> skip(Identity identity) {
    setLastUpdated();
    return new SkipAction(identity, gameState, gameMap, players, nations);
  }

  @Nonnull
  @Override
  public Action<SkipBundle> skipSelf(Identity identity) {
    setLastUpdated();
    return new SkipAction(identity, gameState, gameMap, players, nations, true);
  }

  @Nonnull
  @Override
  public Action<ClaimBundle> claim(Identity identity, TerritoryId... territoryIds) {
    setLastUpdated();
    return new SimpleClaimAction(identity, Set.of(territoryIds), players.getFirst().identity(), gameState, gameMap, nations);
  }

  @Nonnull
  @Override
  public Action<UpdateBundle> update() {
    setLastUpdated();
    return new SimpleUpdateAction(this, gameState, gameMap, players, nations);
  }

  @Nonnull
  @Override
  public CurrentStateBundle currentTurn() {
    Player currentTurnPlayer = players.size() > 0 ? players.getFirst() : null;
    Nation nation = currentTurnPlayer == null ? null : nations.stream().filter(n -> n.identity().equals(currentTurnPlayer.identity())).findAny().orElse(null);
    int claims = nation == null ? -1 : nation.getClaimAmount(map(), nations);
    return new CurrentStateBundle(players.getFirst(), gameState, claims);
  }

  /* Private Methods */

  private void setLastUpdated() {
    this.lastUpdated = Instant.now();
  }

}
