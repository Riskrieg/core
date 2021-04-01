package com.riskrieg.gamemode;

import com.riskrieg.constant.ErrorStr;
import com.riskrieg.gamerule.GameRule;
import com.riskrieg.map.GameMap;
import com.riskrieg.map.graph.Territory;
import com.riskrieg.nation.Nation;
import com.riskrieg.player.Player;
import com.riskrieg.player.PlayerColor;
import com.riskrieg.response.Response;
import java.time.Instant;
import java.util.Deque;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings("unused")
public interface Game {

  UUID getId();

  Instant getCreationTime();

  Instant getLastUpdated();

  String getName();

  String getDescription();

  boolean isEnded();

  Optional<GameMap> getMap();

  Set<GameRule> getGameRules();

  default Optional<GameRule> getGameRule(String name) {
    if (name == null || name.isBlank()) {
      return Optional.empty();
    }
    for (GameRule gameRule : getGameRules()) {
      if (gameRule.getName().equalsIgnoreCase(name)) {
        return Optional.of(gameRule);
      }
    }
    return Optional.empty();
  }

  Deque<Player> getPlayers();

  default Optional<Player> getPlayer(String id) {
    for (Player player : getPlayers()) {
      if (player.getID().equals(id)) {
        return Optional.of(player);
      }
    }
    return Optional.empty();
  }

  default Optional<Player> getPlayer(PlayerColor color) {
    for (Player player : getPlayers()) {
      if (player.getColor().equals(color)) {
        return Optional.of(player);
      }
    }
    return Optional.empty();
  }

  default Response updatePlayerName(Player player, String newName) { // idk
    for (Player p : getPlayers()) {
      if (player.equals(p) && !p.getName().equals(newName)) {
        p.setName(newName);
        return new Response(true);
      }
    }
    return new Response(false, ErrorStr.ERROR_UPDATING_PLAYER_NAME);
  }

  default Response updatePlayerName(String id, String newName) { // idk
    for (Player p : getPlayers()) {
      if (p.getID().equals(id) && !p.getName().equals(newName)) {
        p.setName(newName);
        return new Response(true);
      }
    }
    return new Response(false, ErrorStr.ERROR_UPDATING_PLAYER_NAME);
  }

  default Response updatePlayerName(PlayerColor color, String newName) { // idk
    for (Player p : getPlayers()) {
      if (p.getColor().equals(color) && !p.getName().equals(newName)) {
        p.setName(newName);
        return new Response(true);
      }
    }
    return new Response(false, ErrorStr.ERROR_UPDATING_PLAYER_NAME);
  }

  Set<Nation> getNations();

  default Optional<Nation> getNation(Player player) {
    if (player == null) {
      return Optional.empty();
    }
    for (Nation nation : getNations()) {
      if (player.isLeader(nation)) {
        return Optional.of(nation);
      }
    }
    return Optional.empty();
  }

  default Optional<Nation> getNation(String id) {
    Optional<Player> optPlayer = getPlayer(id);
    if (optPlayer.isPresent()) {
      return getNation(optPlayer.get());
    }
    return Optional.empty();
  }

  default Optional<Nation> getNation(PlayerColor color) {
    Optional<Player> optPlayer = getPlayer(color);
    if (optPlayer.isPresent()) {
      return getNation(optPlayer.get());
    }
    return Optional.empty();
  }

  default Optional<Nation> getNation(Territory territory) {
    for (Nation nation : getNations()) {
      if (nation.getTerritories().contains(territory)) {
        return Optional.of(nation);
      }
    }
    return Optional.empty();
  }

  default Response setGameRule(String name, boolean enabled) {
    return new Response(false, ErrorStr.NO_GAME_RULES);
  }

  Response setMap(GameMap gameMap);

  Response join(Player player);

  Response kick(Player player);

  default Response kick(String id) {
    Optional<Player> optPlayer = getPlayer(id);
    if (optPlayer.isEmpty()) {
      return new Response(false, ErrorStr.PLAYER_COLOR_MISSING);
    }
    return kick(optPlayer.get());
  }

  default Response kick(PlayerColor color) {
    Optional<Player> optPlayer = getPlayer(color);
    if (optPlayer.isEmpty()) {
      return new Response(false, ErrorStr.PLAYER_COLOR_MISSING);
    }
    return kick(optPlayer.get());
  }

  Response formNation(Player player, Territory capital);

  default Response formNation(String id, Territory capital) {
    Optional<Player> optPlayer = getPlayer(id);
    if (optPlayer.isEmpty()) {
      return new Response(false, ErrorStr.PLAYER_MISSING);
    }
    return formNation(optPlayer.get(), capital);
  }

  default Response formNation(PlayerColor color, Territory capital) {
    Optional<Player> optPlayer = getPlayer(color);
    if (optPlayer.isEmpty()) {
      return new Response(false, ErrorStr.PLAYER_MISSING);
    }
    return formNation(optPlayer.get(), capital);
  }

  Response dissolveNation(Nation nation);

  Response start();

  Response turn();

  Response skip(Player player);

  default Response skip(PlayerColor color) {
    Optional<Player> optPlayer = getPlayer(color);
    if (optPlayer.isEmpty()) {
      return new Response(false, ErrorStr.PLAYER_MISSING);
    }
    return skip(optPlayer.get());
  }


  Response claim(Player player, Territory... territories);

  default Response claim(String id, Territory... territories) {
    Optional<Player> optPlayer = getPlayer(id);
    if (optPlayer.isEmpty()) {
      return claim((Player) null, territories);
    }
    return claim(optPlayer.get(), territories);
  }

  default Response claim(PlayerColor color, Territory... territories) {
    Optional<Player> optPlayer = getPlayer(color);
    if (optPlayer.isEmpty()) {
      return claim((Player) null, territories);
    }
    return claim(optPlayer.get(), territories);
  }

  Response update();

  Response nextTurn();

  // Trading

  Response give(Player player, Territory... territories);

  default Response give(PlayerColor color, Territory... territories) {
    Optional<Player> player = getPlayer(color);
    if (player.isEmpty()) {
      return new Response(false, ErrorStr.PLAYER_COLOR_MISSING);
    }
    return give(player.get(), territories);
  }

}
