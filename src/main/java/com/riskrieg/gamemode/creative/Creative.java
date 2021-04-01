package com.riskrieg.gamemode.creative;

import com.riskrieg.constant.Constants;
import com.riskrieg.gamemode.Game;
import com.riskrieg.gamemode.GameMode;
import com.riskrieg.gamerule.GameRule;
import com.riskrieg.map.GameMap;
import com.riskrieg.map.graph.Territory;
import com.riskrieg.nation.CreativeNation;
import com.riskrieg.nation.Nation;
import com.riskrieg.player.Player;
import com.riskrieg.response.Response;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class Creative implements Game {

  private final UUID id;

  private final Instant creationTime;
  private Instant lastUpdated;

  private GameMap gameMap;

  private Deque<Player> players;
  private Set<CreativeNation> nations;

  public Creative() {
    this.id = UUID.randomUUID();
    this.creationTime = Instant.now();
    this.lastUpdated = Instant.now();
    this.players = new ArrayDeque<>();
    this.nations = new HashSet<>();
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public Instant getCreationTime() {
    return creationTime;
  }

  @Override
  public Instant getLastUpdated() {
    return lastUpdated;
  }

  private void setLastUpdated() {
    this.lastUpdated = Instant.now();
  }

  @Override
  public String getName() {
    return GameMode.CREATIVE.getName();
  }

  @Override
  public String getDescription() {
    return GameMode.CREATIVE.getDescription();
  }

  @Override
  public boolean isEnded() {
    return false;
  }

  @Override
  public Optional<GameMap> getMap() {
    return Optional.ofNullable(gameMap);
  }

  @Override
  public Set<GameRule> getGameRules() {
    return new HashSet<>();
  }

  @Override
  public Deque<Player> getPlayers() {
    return players;
  }

  @Override
  public Set<Nation> getNations() {
    return new HashSet<>(nations);
  }

  @Override
  public Response setMap(GameMap gameMap) {
    if (players.size() == 0) {
      return new Response(false, "You must join the game in order to select a map.");
    }
    if (gameMap == null) {
      return new Response(false, "Invalid map.");
    }
    this.gameMap = gameMap;
    this.nations = new HashSet<>();
    setLastUpdated();
    return new Response(true);
  }

  @Override
  public Response join(Player player) {
    if (player == null) {
      return new Response(false, "Invalid player.");
    }
    if (players.contains(player)) {
      return new Response(false, "You have already joined the game.");
    }
    if (players.size() >= Constants.MAX_PLAYERS) {
      return new Response(false, "The game is too full to join.");
    }
    if (players.stream().anyMatch(p -> p.getColor().equals(player.getColor()))) {
      return new Response(false, "Another player has already joined as that color.");
    }
    players.add(player);
    setLastUpdated();
    return new Response(true);
  }

  @Override
  public Response kick(Player player) {
    if (player == null || !getPlayers().contains(player)) {
      return new Response(false, "Player is not in the game.");
    }
    getNation(player).ifPresent(this::dissolveNation);
    players.remove(player);
    setLastUpdated();
    return new Response(true);
  }


  @Override
  public Response formNation(Player player, Territory capital) {
    if (player == null || !getPlayers().contains(player)) {
      return new Response(false, "Player is not in the game. You must join the game first.");
    }
    if (gameMap != null && nations.stream().mapToInt(o -> o.getTerritories().size()).sum() == gameMap.getTerritories().size()) {
      kick(player);
      return new Response(false, "There are no territories for you to select, so you have been removed from the game.");
    }
    CreativeNation nation = new CreativeNation(player, capital);
    if (nations.stream().anyMatch(n -> n.getCapital().equals(capital))) {
      return new Response(false, "That territory is already owned by someone else.");
    }
    if (nations.contains(nation)) {
      dissolveNation(nation);
    }
    nations.add(nation);
    setLastUpdated();
    return new Response(true);
  }

  @Override
  public Response dissolveNation(Nation nation) {
    if (!(nation instanceof CreativeNation)) {
      return new Response(false, "Nation does not exist.");
    }
    if (!nations.contains(nation)) {
      return new Response(false, "Player is not in game.");
    }
    nations.remove(nation);
    setLastUpdated();
    return new Response(true);
  }

  @Override
  public Response start() {
    return new Response(false, "Unavailable in this game mode.");
  }

  @Override
  public Response turn() {
    return new Response(false, "Unavailable in this game mode.");
  }

  @Override
  public Response skip(Player player) {
    return new Response(false, "Unavailable in this game mode.");
  }

  @Override
  public Response claim(Player player, Territory... territories) {
    return new Response(false, "Unavailable in this game mode.");
  }

  @Override
  public Response update() {
    return new Response(false, "Unavailable in this game mode.");
  }

  @Override
  public Response nextTurn() {
    return new Response(false, "Unavailable in this game mode.");
  }

  @Override
  public Response give(Player player, Territory... territories) {
    if (getMap().isEmpty()) {
      return new Response(false, "No map selected.");
    }
    if (territories == null || territories.length == 0) {
      return new Response(false, "Invalid territories.");
    }
    Optional<Nation> optNation = getNation(player);
    if (optNation.isEmpty()) {
      return new Response(false, "Player is not in the game.");
    }
    Nation nation = optNation.get();

    Set<Territory> invalidTerritories = new HashSet<>();
    for (Territory territory : territories) {
      if (getMap().get().getTerritory(territory.name()).isEmpty()) {
        invalidTerritories.add(territory);
      }
    }

    if (invalidTerritories.size() > 0) {
      return new Response(false, "The following territories do not exist on this map: "
          + invalidTerritories.stream().map(Territory::name).collect(Collectors.joining(", ")).trim());
    }

    for (Nation n : nations) {
      for (Territory t : territories) {
        if (n.getTerritories().contains(t)) {
          n.removeTerritory(t);
        }
      }
    }

    for (Territory t : territories) {
      nation.addTerritory(t);
    }
    setLastUpdated();
    return new Response(true);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Creative creative = (Creative) o;
    return id.equals(creative.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

}
