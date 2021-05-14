package com.riskrieg.gamemode.conquest;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.riskrieg.constant.Constants;
import com.riskrieg.constant.ErrorStr;
import com.riskrieg.gamemode.Game;
import com.riskrieg.gamemode.GameMode;
import com.riskrieg.gamemode.IAlliances;
import com.riskrieg.gamerule.GameRule;
import com.riskrieg.gamerule.rules.Alliances;
import com.riskrieg.gamerule.rules.JoinAnyTime;
import com.riskrieg.gamerule.rules.RandomTurnOrder;
import com.riskrieg.map.GameMap;
import com.riskrieg.map.graph.Territory;
import com.riskrieg.nation.AllianceNation;
import com.riskrieg.nation.ConquestNation;
import com.riskrieg.nation.Dice;
import com.riskrieg.nation.Nation;
import com.riskrieg.player.Player;
import com.riskrieg.player.PlayerIdentifier;
import com.riskrieg.response.Response;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

public class Conquest implements Game, IAlliances {

  private final UUID id;

  private final Instant creationTime;
  private Instant lastUpdated;

  private GameMap gameMap;
  private GameState gameState;

  private final Set<GameRule> gameRules;
  private Deque<Player> players;
  private Set<ConquestNation> nations;

  public Conquest() {
    id = UUID.randomUUID();
    this.creationTime = Instant.now();
    this.lastUpdated = Instant.now();
    this.gameState = GameState.SETUP;
    this.gameRules = new TreeSet<>();
    this.gameRules.add(new Alliances());
    this.gameRules.add(new RandomTurnOrder());
    this.gameRules.add(new JoinAnyTime());
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

  /**
   * Getters
   **/

  @Override
  public String getName() {
    return GameMode.CONQUEST.getName();
  }

  @Override
  public String getDescription() {
    return GameMode.CONQUEST.getDescription();
  }

  @Override
  public boolean isEnded() {
    return gameState.equals(GameState.ENDED);
  }

  @Override
  public Optional<GameMap> getMap() {
    return Optional.ofNullable(gameMap);
  }

  @Override
  public Set<GameRule> getGameRules() {
    return gameRules;
  }

  @Override
  public Deque<Player> getPlayers() {
    return players;
  }

  @Override
  public Set<Nation> getNations() {
    return new HashSet<>(nations);
  }

  /**
   * Setters
   **/

  @Override
  public Response setGameRule(String name, boolean enabled) {
    return switch (gameState) {
      case RUNNING, ENDED -> new Response(false, "You can only change game rules during the setup phase.");
      case SETUP -> {
        Optional<GameRule> gameRuleOptional = getGameRule(name);
        if (gameRuleOptional.isPresent()) {
          gameRuleOptional.get().setEnabled(enabled);
          setLastUpdated();
          yield new Response(true); //  "The " + gameRule.getDisplayName() + " game rule has been set to " + gameRule.isEnabled() + "."
        } else {
          yield new Response(false, "That game rule doesn't exist in this game mode.");
        }
      }
    };
  }

  @Override
  public Response setMap(GameMap gameMap) {
    return switch (gameState) {
      case RUNNING, ENDED -> new Response(false, "You can only set the map during the setup phase.");
      case SETUP -> {
        if (gameMap == null) {
          yield new Response(false, "Invalid map.");
        }
        this.gameMap = gameMap;
        this.nations = new HashSet<>();
        setLastUpdated();
        yield new Response(true); // "The map has been set to " + gameMap.getDisplayName() + "."
      }
    };
  }

  /**
   * Setup
   **/

  @Override
  public Response join(Player player) {
    return switch (gameState) {
      case ENDED -> new Response(false, "You can only join the game during the setup phase.");
      case RUNNING -> {
        Optional<GameRule> optGr = getGameRule("join-any-time");
        if (optGr.isPresent() && optGr.get().isEnabled()) {
          yield pJoin(player);
        } else {
          yield new Response(false, "You can only join the game during the setup phase.");
        }
      }
      case SETUP -> pJoin(player);
    };
  }

  private Response pJoin(Player player) {
    if (player == null) {
      return new Response(false, ErrorStr.INVALID_PLAYER);
    }
    if (players.contains(player) || players.stream().anyMatch(p -> p.getID().equals(player.getID()))) {
      return new Response(false, "You have already joined the game.");
    }
    if (players.size() >= Constants.MAX_PLAYERS) {
      return new Response(false, "The game is too full to join.");
    }
    if (players.stream().anyMatch(p -> p.getColor().equals(player.getColor()))) {
      return new Response(false, "Another player has already chosen that color.");
    }
    if (gameMap != null && nations.stream().mapToInt(o -> o.getTerritories().size()).sum() == gameMap.getTerritories().size()) {
      return new Response(false, "There are no territories for you to select, so you cannot join.");
    }
    if (gameState.equals(GameState.RUNNING)) {
      // Get first player before adding.
      // Add new player.
      // Resort all players.
      // Iterate through until back at proper first player.
      Optional<GameRule> optGr = getGameRule("random-turns");
      if (optGr.isPresent() && optGr.get().isEnabled()) {
        players.add(player);
      } else {
        Player first = players.getFirst();
        players.add(player);
        players = new ArrayDeque<>(new TreeSet<>(players));

        int counter = 0; // Failsafe. TODO: May or may not work.
        while (!players.getFirst().equals(first) && counter < 32) {
          players.addLast(players.getFirst());
          counter++;
        }

        if (counter == 32) {
          players = new ArrayDeque<>(new TreeSet<>(players));
        }
      }
    } else {
      players.add(player);
    }
    setLastUpdated();
    return new Response(true);
  }

  @Override
  public Response kick(Player player) {
    if (player == null || !getPlayers().contains(player)) {
      return new Response(false, ErrorStr.PLAYER_MISSING);
    }
    getNation(player).ifPresent(this::dissolveNation);
    players.remove(player);
    setLastUpdated();
    return new Response(true);
  }

  @Override
  public Response formNation(Player player, Territory capital) {
    return switch (gameState) {
      case ENDED -> new Response(false, "You can only select a capital during the setup phase.");
      case RUNNING -> {
        Optional<GameRule> optGr = getGameRule("join-any-time");
        if (optGr.isPresent() && optGr.get().isEnabled()) {
          if (nations.stream().anyMatch(n -> n.getLeaderIdentifier().equals(player.getIdentifier()) && n.getCapital() != null)) {
            yield new Response(false, "You have already selected a capital.");
          }
          yield pFormNation(player, capital);
        } else {
          yield new Response(false, "You can only select a capital during the setup phase.");
        }
      }
      case SETUP -> pFormNation(player, capital);
    };
  }

  private Response pFormNation(Player player, Territory capital) {
    if (player == null || !getPlayers().contains(player)) {
      return new Response(false, ErrorStr.PLAYER_MISSING);
    }

    ConquestNation nation = new ConquestNation(player, capital);
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
    if (!(nation instanceof ConquestNation)) {
      return new Response(false, ErrorStr.INVALID_NATION);
    }
    if (!nations.contains(nation)) {
      return new Response(false, ErrorStr.PLAYER_MISSING);
    }
    dissolveAlliances((AllianceNation) nation);
    nations.remove(nation);
    setLastUpdated();
    return new Response(true);
  }

  @Override
  public Response start() {
    return switch (gameState) {
      case RUNNING, ENDED -> new Response(false, "You can only begin a game during the setup phase.");
      case SETUP -> {
        if (players.size() < Constants.MIN_PLAYERS) {
          yield new Response(false, "Two or more players are required to play.");
        }
        if (getMap().isEmpty()) {
          yield new Response(false, "You must select a valid map first.");
        }
        if (nations.size() < players.size()) {
          yield new Response(false, "Not all players have selected a capital.");
        }
        if (nations.size() > players.size()) {
          yield new Response(false, "There are more nations than players. This should never happen, please report it as a bug.");
        }
        if (players.stream().allMatch(Player::isComputer)) { // TODO: Check other areas of code to make sure a game ends if all players are computers at any point.
          yield new Response(false, "You need at least one human player in order to start a game.");
        }
        Optional<GameRule> randomTurns = getGameRule("random-turns");
        if (randomTurns.isPresent() && randomTurns.get().isEnabled()) {
          List<Player> playerList = new ArrayList<>(players);
          Collections.shuffle(playerList);
          players = new ArrayDeque<>(playerList);
        } else {
          players = new ArrayDeque<>(new TreeSet<>(players));
        }
        this.gameState = GameState.RUNNING;
        setLastUpdated();
        yield new Response(true);
      }
    };
  }

  /**
   * Running
   **/

  @Override
  public Response turn() {
    return switch (gameState) {
      case SETUP, ENDED -> new Response(false, "You can only check whose turn it is while the game is active.");
      case RUNNING -> new Response(true, "It is currently " + getPlayers().getFirst().getName() + "'s turn.");
    };
  }

  @Override
  public Response skip(Player player) {
    return switch (gameState) {
      case SETUP, ENDED -> new Response(false, "You can only skip someone while the game is active.");
      case RUNNING -> {
        if (players.getFirst().equals(player)) {
          players.addLast(players.removeFirst());
          yield new Response(true, player.getName() + " has been skipped.");
        } else {
          yield new Response(false, "You can only skip a player if it is their turn.");
        }
      }
    };
  }

  @Override
  public Response claim(Player player, Territory... territories) {
    return switch (gameState) {
      case SETUP, ENDED -> new Response(false, "You can only claim territories after the game begins.");
      case RUNNING -> {
        Optional<Nation> optNation = getNation(player);
        if (optNation.isEmpty()) {
          yield new Response(false, ErrorStr.PLAYER_MISSING);
        }
        Nation nation = optNation.get();
        if (!(nation instanceof ConquestNation)) {
          yield new Response(false, ErrorStr.INVALID_NATION);
        }
        if (getMap().isEmpty()) {
          yield new Response(false, ErrorStr.MAP_MISSING);
        }
        if (territories == null || territories.length == 0) {
          yield new Response(false, ErrorStr.INVALID_TERRITORY);
        }
        Set<Territory> invalidTerritories = new HashSet<>();
        Set<Territory> ownedTerritories = new HashSet<>();
        Set<Territory> notBordering = new HashSet<>();
        for (Territory territory : territories) {
          if (getMap().get().getTerritory(territory.name()).isEmpty()) {
            invalidTerritories.add(territory);
          } else if (nation.getTerritories().contains(territory)) {
            ownedTerritories.add(territory);
          } else if (nation.getTerritories().stream().noneMatch(t -> getMap().get().neighbors(territory, t))) {
            notBordering.add(territory);
          }
        }
        if (!players.getFirst().equals(player)) {
          yield new Response(false, "You can only claim territories on your turn.");
        }
        if (invalidTerritories.size() > 0) {
          yield new Response(false, "The following territories do not exist on this map: "
              + invalidTerritories.stream().map(Territory::name).collect(Collectors.joining(", ")).trim());
        }
        if (ownedTerritories.size() > 0) {
          yield new Response(false, "You cannot claim territory you already own. You own the following territories: "
              + ownedTerritories.stream().map(Territory::name).collect(Collectors.joining(", ")).trim());
        }
        if (notBordering.size() > 0) {
          yield new Response(false, "You can only claim territories that you border. You do not border the following territories: "
              + notBordering.stream().map(Territory::name).collect(Collectors.joining(", ")).trim());
        }
        Set<Territory> alliedTerritories = new HashSet<>();
        for (Territory territory : territories) {
          Optional<Nation> optOwner = getNation(territory);
          if (optOwner.isPresent() && allied((AllianceNation) nation, (AllianceNation) optOwner.get())) {
            alliedTerritories.add(territory);
          }
        }
        if (alliedTerritories.size() > 0) {
          yield new Response(false, "You cannot claim territories that belong to allies. The following territories belong to allies: "
              + alliedTerritories.stream().map(Territory::name).collect(Collectors.joining(", ")).trim());
        }
        int claims = ((ConquestNation) nation).getClaimAmount(this);
        if (claims != territories.length) {
          yield new Response(false, "You have "
              + claims + (claims == 1 ? " claim" : " claims") + " but you are trying to claim "
              + territories.length + (territories.length == 1 ? " territory" : " territories") + ".");
        }
        Set<Territory> claimed = new HashSet<>();
        Set<Territory> taken = new HashSet<>();
        Set<Territory> defended = new HashSet<>();
        for (Territory territory : territories) {
          Optional<Nation> optDefender = getNation(territory);
          if (optDefender.isPresent()) { // attack
            boolean successfulAttack = rollAttack(nation, optDefender.get(), territory);
            if (successfulAttack) {
              optDefender.get().removeTerritory(territory);
              nation.addTerritory(territory);
              taken.add(territory);
            } else {
              defended.add(territory);
            }
          } else { // claim
            nation.addTerritory(territory);
            claimed.add(territory);
          }
        }
        StringBuilder sb = new StringBuilder();
        if (claimed.size() > 0) {
          sb.append(player.getName()).append(" has claimed: ")
              .append(claimed.stream().map(Territory::name).collect(Collectors.joining(", ")).trim()).append("\n");
        }
        if (taken.size() > 0) {
          sb.append(player.getName()).append(" has taken: ")
              .append(taken.stream().map(Territory::name).collect(Collectors.joining(", ")).trim()).append("\n");
        }

        SetMultimap<Player, Territory> ownerSet = HashMultimap.create();
        for (Territory d : defended) {
          for (Nation n : nations) {
            if (n.getTerritories().contains(d)) {
              getPlayer(n.getLeaderIdentifier().id()).ifPresent(value -> ownerSet.put(value, d));
            }
          }
        }

        if (defended.size() > 0) {
          for (Player p : ownerSet.keySet()) {
            Collection<Territory> ownerTerritories = ownerSet.get(p);
            sb.append(p.getName()).append(" has defended: ")
                .append(ownerTerritories.stream().map(Territory::name).collect(Collectors.joining(", ")).trim()).append("\n");
          }
        }

        sb.append("\n");
        yield new Response(true, sb.toString());
      }
    };
  }

  /**
   * Attacker attacks defender. Get number of attacker's territories that neighbor territory. Attacker gets that many 6-sided dice. Get number of defender's territories that
   * neighbor territory. Defender gets that many 6-sided dice. Minimum of 1 roll each. Take maximum roll of each. Defenders win ties.
   *
   * @param attacker  The nation attacking.
   * @param defender  The nation defending.
   * @param territory The territory belonging to the defender being attacked by the attacker.
   * @return Whether the attack was successful.
   */
  private boolean rollAttack(Nation attacker, Nation defender, Territory territory) {
    int attackRolls = 0;
    int defenseRolls = 0;
    int attackSides = 8;
    int defenseSides = 6;
    if (getMap().isPresent()) {
      Set<Territory> neighbors = getMap().get().getNeighbors(territory);
      for (Territory neighbor : neighbors) {
        if (attacker.getTerritories().contains(neighbor)) {
          attackRolls++;
        } else if (defender.getTerritories().contains(neighbor)) {
          defenseRolls++;
        }
      }
      if (neighbors.contains(attacker.getCapital())) {
        attackRolls += 1 + Constants.CAPITAL_ATTACK_ROLL_BOOST;
      }
      if (territory.equals(defender.getCapital())) {
        defenseSides += 1 + Constants.CAPITAL_DEFENSE_ROLL_BOOST;
      }
      Dice attackDice = new Dice(attackSides, Math.max(1, attackRolls)); // just in case, I think the rolls should always be at least 1 though.
      Dice defenseDice = new Dice(defenseSides, Math.max(1, defenseRolls));
      int attackerMax = Arrays.stream(attackDice.roll()).summaryStatistics().getMax();
      int defenderMax = Arrays.stream(defenseDice.roll()).summaryStatistics().getMax();
      return attackerMax > defenderMax;
    }
    return false;
  }

  @Override
  public Response update() {
    switch (gameState) {
      case SETUP -> {
        return new Response(true);
      }
      case RUNNING -> {
        StringBuilder sb = new StringBuilder();
        /* Defeated Check */
        if (nations.stream().anyMatch(n -> n.getTerritories().size() == 0)) {
          Set<Nation> defeated = new HashSet<>();
          for (Nation n : nations) {
            if (n.getTerritories().size() == 0) {
              getPlayer(n.getLeaderIdentifier().color()).ifPresent(p -> sb.append(p.getName()).append(" has been defeated.").append("\n"));
              defeated.add(n);
            }
          }
          defeated.forEach(n -> kick(n.getLeaderIdentifier().color()));
        }

        sb.append("\n");

        /* End State Check */
        if (players.size() > 1 && nations.stream().allMatch(n -> n.getClaimAmount(this) == 0)) {
          sb.append("A stalemate has been reached! The game is now over.");
          gameState = GameState.ENDED;
        } else if (players.size() == 1) {
          sb.append(players.getFirst().getName()).append(" has won the game!");
          gameState = GameState.ENDED;
        } else if (players.size() == 0) {
          sb.append("All players are defeated. The game is now over.");
          gameState = GameState.ENDED;
        }

        /* Skip Check */ // TODO: If you just got 0 claims this exact turn, it skips incorrectly, so check player after players.getFirst()
//        Optional<Nation> optNation = getNation(players.getFirst());
//        while (optNation.isPresent() && ((ConquestNation) optNation.get()).getClaimAmount(this) == 0) {
//          sb.append(players.getFirst().getName()).append(" has no claims and has been skipped.").append("\n");
//          skip(players.getFirst());
//          optNation = getNation(players.getFirst());
//        }

        if (sb.isEmpty() && sb.toString().isBlank()) {
          return new Response(true);
        } else {
          return new Response(true, sb.toString());
        }
      }
    }
    return new Response(false);
  }

  @Override
  public Response nextTurn() {
    if (players.size() == 1) {
      return new Response(false, "There is only one person in the game.");
    }
    players.addLast(players.removeFirst());
    return new Response(true);
  }

  /**
   * Alliances
   **/

  @Override
  public boolean allied(AllianceNation a, AllianceNation b) {
    if (a instanceof ConquestNation && b instanceof ConquestNation && nations.contains(a) && nations.contains(b)) {
      return a.isAlly(b);
    }
    return false;
  }

  @Override
  public boolean allied(String idA, String idB) {
    Optional<Nation> optNA = getNation(idA);
    Optional<Nation> optNB = getNation(idB);
    if (optNA.isPresent() && optNB.isPresent()) {
      return allied((AllianceNation) optNA.get(), (AllianceNation) optNB.get());
    }
    return false;
  }

  @Override
  public Response ally(AllianceNation a, AllianceNation b) {
    Optional<GameRule> alliances = getGameRule("alliances");
    if (alliances.isEmpty()) {
      return new Response(false, "That game rule does not exist.");
    }
    if (!alliances.get().isEnabled()) {
      return new Response(false, "The alliances game rule is disabled.");
    }
    return switch (gameState) {
      case SETUP, ENDED -> new Response(false, "You can only make alliances after the game begins.");
      case RUNNING -> {
        if (a instanceof ConquestNation && b instanceof ConquestNation && nations.contains(a) && nations.contains(b)) {
          if (a.equals(b)) {
            yield new Response(false, "You cannot form an alliance with yourself.");
          }
          if (a.addAlly(b)) {
            setLastUpdated();
            yield new Response(true);
          } else {
            yield new Response(false, "Could not add as ally.");
          }
        } else {
          yield new Response(false, ErrorStr.PLAYER_MISSING);
        }
      }
    };
  }

  @Override
  public Response ally(String idA, String idB) {
    Optional<Nation> optNA = getNation(idA);
    Optional<Nation> optNB = getNation(idB);
    if (optNA.isPresent() && optNB.isPresent()) {
      return ally((AllianceNation) optNA.get(), (AllianceNation) optNB.get());
    }
    return new Response(false, ErrorStr.PLAYER_MISSING);
  }

  @Override
  public Response unally(AllianceNation a, AllianceNation b) {
    Optional<GameRule> alliances = getGameRule("alliances");
    if (alliances.isEmpty()) {
      return new Response(false, "That game rule does not exist.");
    }
    if (!alliances.get().isEnabled()) {
      return new Response(false, "The alliances game rule is disabled.");
    }
    return switch (gameState) {
      case SETUP, ENDED -> new Response(false, "You can only break alliances after the game begins.");
      case RUNNING -> {
        if (a instanceof ConquestNation && b instanceof ConquestNation && nations.contains(a) && nations.contains(b)) {
          if (a.equals(b)) {
            yield new Response(false, "You cannot break an alliance with yourself.");
          }
          if (a.removeAlly(b)) {
            setLastUpdated();
            yield new Response(true);
          } else {
            yield new Response(false, "Could not remove ally.");
          }
        } else {
          yield new Response(false, ErrorStr.PLAYER_MISSING);
        }
      }
    };
  }

  @Override
  public Response unally(String idA, String idB) {
    Optional<Nation> optNA = getNation(idA);
    Optional<Nation> optNB = getNation(idB);
    if (optNA.isPresent() && optNB.isPresent()) {
      return unally((AllianceNation) optNA.get(), (AllianceNation) optNB.get());
    }
    return new Response(false, ErrorStr.PLAYER_MISSING);
  }

  @Override
  public Response dissolveAlliances(AllianceNation nation) {
    Optional<GameRule> alliances = getGameRule("alliances");
    if (alliances.isEmpty()) {
      return new Response(false, "That game rule does not exist.");
    }
    if (!alliances.get().isEnabled()) {
      return new Response(false, "The alliances game rule is disabled.");
    }
    if (nation instanceof ConquestNation && nations.contains(nation)) {
      Set<PlayerIdentifier> tempAllies = new HashSet<>(nation.getAllies());
      tempAllies.forEach(nation::removeAlly);
      setLastUpdated();
      return new Response(true);
    } else {
      return new Response(false, ErrorStr.PLAYER_MISSING);
    }
  }

  @Override
  public Response dissolveAlliances(String id) {
    Optional<Nation> optNation = getNation(id);
    if (optNation.isPresent()) {
      return dissolveAlliances((AllianceNation) optNation.get());
    }
    return new Response(false, ErrorStr.PLAYER_MISSING);
  }

  /**
   * Trading -- On pause for now
   **/

  @Override
  public Response give(Player player, Territory... territories) {
    return new Response(false, "Unavailable in this game mode.");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Conquest conquest = (Conquest) o;
    return id.equals(conquest.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

}
