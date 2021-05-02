package com.riskrieg.core.api;

import com.riskrieg.core.api.gamemode.GameID;
import com.riskrieg.core.api.gamemode.GameMode;
import com.riskrieg.core.api.gamemode.brawl.BrawlMode;
import com.riskrieg.core.api.gamemode.classic.ClassicMode;
import com.riskrieg.core.api.gamemode.conquest.ConquestMode;
import com.riskrieg.core.api.gamemode.creative.CreativeMode;
import com.riskrieg.core.api.gamemode.siege.SiegeMode;
import com.riskrieg.core.api.nation.Nation;
import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.unsorted.gamemode.GameModeType;
import com.riskrieg.core.unsorted.gamemode.GameState;
import com.riskrieg.core.unsorted.gamemode.Moment;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public final class Save {

  private final GameModeType gameType;
  private final GameID id;
  private final Moment creationTime;
  private final Moment lastUpdated;

  private final GameState gameState;
  private final String mapCodeName;

  private final Deque<Player> players;
  private final Set<Nation> nations;

  public <T extends GameMode> Save(T game) {
    this.id = game.getId();
    this.creationTime = Moment.of(game.creationTime());
    this.lastUpdated = Moment.of(game.lastUpdated());
    this.gameState = game.gameState();
    if (game.map().isSet()) {
      this.mapCodeName = game.map().getMapName().name();
    } else {
      this.mapCodeName = null;
    }
    this.players = new ArrayDeque<>(game.players());
    this.nations = new HashSet<>(game.nations());
    if (game instanceof ClassicMode) {
      this.gameType = GameModeType.CLASSIC;
    } else if (game instanceof ConquestMode) {
      this.gameType = GameModeType.CONQUEST;
    } else if (game instanceof SiegeMode) {
      this.gameType = GameModeType.SIEGE;
    } else if (game instanceof BrawlMode) {
      this.gameType = GameModeType.BRAWL;
    } else if (game instanceof CreativeMode) {
      this.gameType = GameModeType.CREATIVE;
    } else {
      throw new IllegalArgumentException("provided game mode is not supported");
    }
  }

  public GameModeType getGameType() {
    return gameType;
  }

  public GameID id() {
    return id;
  }

  public Moment creationTime() {
    return creationTime;
  }

  public Moment lastUpdated() {
    return lastUpdated;
  }

  public GameState gameState() {
    return gameState;
  }

  public String mapCodeName() {
    return mapCodeName;
  }

  public Deque<Player> players() {
    return players;
  }

  public Set<Nation> nations() {
    return nations;
  }

}
