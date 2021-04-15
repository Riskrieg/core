package com.riskrieg.save;

import com.riskrieg.api.gamemode.Conquest;
import com.riskrieg.api.nation.Nation;
import com.riskrieg.api.player.Player;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ConquestSave {

  private final UUID id;
  private final Instant creationTime;
  private final Instant lastUpdated;
  private final String mapCodeName;
  private final Deque<Player> players;
  private final Set<Nation> nations;

  public ConquestSave(Conquest conquest) {
    this.id = conquest.id();
    this.creationTime = conquest.creationTime();
    this.lastUpdated = conquest.lastUpdated();
    this.mapCodeName = conquest.map().mapName().name();
    this.players = new ArrayDeque<>(conquest.players());
    this.nations = new HashSet<>(conquest.nations());
  }

}
