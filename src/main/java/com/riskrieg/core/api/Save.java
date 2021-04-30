package com.riskrieg.core.api;

import com.riskrieg.core.api.gamemode.GameID;
import com.riskrieg.core.api.gamemode.GameMode;
import com.riskrieg.core.api.gamemode.conquest.Conquest;
import com.riskrieg.core.api.nation.Nation;
import com.riskrieg.core.api.player.Player;
import com.riskrieg.core.unsorted.gamemode.GameModeType;
import com.riskrieg.core.unsorted.gamemode.GameState;
import com.riskrieg.core.unsorted.gamemode.Moment;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class Save {

  private final GameModeType gameType;
  private final GameID id;
  private final Moment creationTime;
  private final Moment lastUpdated;

  private final GameState gameState;
  private final String mapCodeName;

  private final Deque<Player> players;
  private final Set<Nation> nations;

  public <T extends GameMode> Save(T game) {
    if (game instanceof Conquest) {
      Conquest conquest = (Conquest) game;
      this.gameType = GameModeType.CONQUEST;
      this.id = conquest.getId();
      this.creationTime = Moment.of(conquest.creationTime());
      this.lastUpdated = Moment.of(conquest.lastUpdated());
      this.gameState = conquest.gameState();
      if (conquest.map().isSet()) {
        this.mapCodeName = conquest.map().getMapName().name();
      } else {
        this.mapCodeName = null;
      }
      this.players = new ArrayDeque<>(conquest.players());
      this.nations = new HashSet<>(conquest.nations());
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
