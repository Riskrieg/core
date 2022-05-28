package com.riskrieg.core.api;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.riskrieg.core.api.color.ColorPalette;
import com.riskrieg.core.api.game.Attack;
import com.riskrieg.core.api.game.Game;
import com.riskrieg.core.api.game.GameConstants;
import com.riskrieg.core.api.game.GamePhase;
import com.riskrieg.core.api.game.entity.nation.Nation;
import com.riskrieg.core.api.game.map.GameMap;
import com.riskrieg.core.api.game.mode.Conquest;
import com.riskrieg.core.api.game.mode.Mock;
import com.riskrieg.core.api.game.territory.Claim;
import com.riskrieg.core.api.game.territory.GameTerritory;
import com.riskrieg.core.api.game.territory.TerritoryType;
import com.riskrieg.core.api.group.Group;
import com.riskrieg.core.api.identifier.GameIdentifier;
import com.riskrieg.core.api.identifier.GroupIdentifier;
import com.riskrieg.core.api.identifier.PlayerIdentifier;
import com.riskrieg.core.api.identifier.TerritoryIdentifier;
import com.riskrieg.core.decode.RkmDecoder;
import com.riskrieg.core.util.game.GameUtil;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

public class TestApi { // TODO: Implement comprehensive tests

  @Test
  public void testRepositoryOperations() { // TODO: Eliminate this test
    Riskrieg api = RiskriegBuilder.createLocal(Path.of("res/")).build();
    Collection<Group> groups = api.retrieveAllGroups().complete();

    Group group = api.createGroup(GroupIdentifier.of("12345")).complete();
    Game game = group.createGame(GameIdentifier.of("123"), Conquest.class).complete();

    var batch = ColorPalette.standard();
    for (int i = 0; i < batch.set().size(); i++) {
      assertEquals(i, batch.get(i).id());
    }
    assertEquals(0, ColorPalette.standard().get(-1).id());
    assertEquals(15, ColorPalette.standard().get(16).id());

    game.addPlayer(PlayerIdentifier.of("1"), "One").complete();
    Nation nation = game.createNation(ColorPalette.standard().get(3), PlayerIdentifier.of("1")).complete();

    RkmDecoder decoder = new RkmDecoder();

    Attack setup = (attacker, defender, territory, map, claims, constants) -> true;

    Attack attack = (attacker, defender, territory, map, claims, constants) -> {
      if (attacker == null) {
        return false;
      }
      if (defender == null) {
        return true;
      }
      if (attacker.equals(defender)) {
        return false;
      }
      if (!GameUtil.nationClaimsTerritory(defender, territory.identifier(), claims)) {
        return false;
      }
      int attackRolls = 1;
      int defenseRolls = 1;
      int attackSides = 8;
      int defenseSides = 6;
      var neighbors = map.neighborsAsIdentifiers(territory.identifier());
      for (TerritoryIdentifier neighbor : neighbors) {
        var attackerTerritories = GameUtil.getClaimSet(attacker, claims).stream().map(Claim::territory).map(GameTerritory::identifier).collect(Collectors.toSet());
        var defenderTerritories = GameUtil.getClaimSet(defender, claims).stream().map(Claim::territory).map(GameTerritory::identifier).collect(Collectors.toSet());
        if (attackerTerritories.contains(neighbor)) {
          if (GameUtil.territoryIsOfType(neighbor, TerritoryType.CAPITAL, claims)) {
            attackRolls += 1 + constants.capitalAttackBonus();
          } else {
            attackRolls++;
          }
        } else if (defenderTerritories.contains(neighbor)) {
          defenseRolls++;
          if (GameUtil.territoryIsOfType(territory.identifier(), TerritoryType.CAPITAL, claims)) {
            defenseSides += 1 + constants.capitalDefenseBonus();
          }
        }
      }
      Dice attackDice = new Dice(attackSides, attackRolls);
      Dice defenseDice = new Dice(defenseSides, defenseRolls);
      int attackerMax = Arrays.stream(attackDice.roll()).summaryStatistics().getMax();
      int defenderMax = Arrays.stream(defenseDice.roll()).summaryStatistics().getMax();
      return attackerMax > defenderMax;
    };

    try {
      game.selectMap(decoder.decode(Path.of("res/maps/antarctica.rkm"))).complete();
    } catch (IOException | NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }

    game.claim(attack, nation.identifier(), GameTerritory.of(TerritoryIdentifier.of("1F"), TerritoryType.CAPITAL)).complete();

    group.saveGame(game).complete();

    Game testGame = group.retrieveGame(GameIdentifier.of("123")).complete();
    assertNotNull(testGame);
    assertEquals("123", testGame.identifier().id());
    assertEquals(16, testGame.palette().size());
    assertTrue(testGame instanceof Conquest);

    group.deleteGame(GameIdentifier.of("123")).complete();


    /*
     * /create mode opt:colors
     * Game objects should define their own list of valid game states, in addition to the base states required of all games.
     */

  }

  @Test
  public void testRiskriegNullThrows() {
    // Arrange
    Riskrieg api = RiskriegBuilder.createLocal(Path.of("res/")).build();

    // Act & Assert
    assertThrowsExactly(NullPointerException.class, () -> api.createGroup(null).complete());
    assertThrowsExactly(NullPointerException.class, () -> api.createGroup(null).queue());

    assertThrowsExactly(NullPointerException.class, () -> api.retrieveGroup(null).complete());
    assertThrowsExactly(NullPointerException.class, () -> api.retrieveGroup(null).queue());

    assertThrowsExactly(NullPointerException.class, () -> api.deleteGroup(null).complete());
    assertThrowsExactly(NullPointerException.class, () -> api.deleteGroup(null).queue());
  }

  @Test
  public void testGroupNullThrows() {
    // Arrange
    Riskrieg api = RiskriegBuilder.createLocal(Path.of("res/")).build();
    Group group = api.createGroup(GroupIdentifier.of("test-group")).complete();

    // Act & Assert
    assertThrowsExactly(NullPointerException.class, () -> group.createGame(null).complete());
    assertThrowsExactly(NullPointerException.class, () -> group.createGame(null).queue());
    assertThrowsExactly(NullPointerException.class, () -> group.createGame(null, null).complete());
    assertThrowsExactly(NullPointerException.class, () -> group.createGame(null, null).queue());
    assertThrowsExactly(NullPointerException.class, () -> group.createGame(null, null, null).complete());
    assertThrowsExactly(NullPointerException.class, () -> group.createGame(null, null, null).queue());
    assertThrowsExactly(NullPointerException.class, () -> group.createGame(null, null, null, null).complete());
    assertThrowsExactly(NullPointerException.class, () -> group.createGame(null, null, null, null).queue());

    assertThrowsExactly(NullPointerException.class, () -> group.retrieveGame(null).complete());
    assertThrowsExactly(NullPointerException.class, () -> group.retrieveGame(null).queue());

    assertThrowsExactly(NullPointerException.class, () -> group.saveGame(null).complete());
    assertThrowsExactly(NullPointerException.class, () -> group.saveGame(null).queue());

    assertThrowsExactly(NullPointerException.class, () -> group.deleteGame(null).complete());
    assertThrowsExactly(NullPointerException.class, () -> group.deleteGame(null).queue());

    cleanup(group);
  }

  @Test
  public void testGameNullThrows() {
    // TODO: Write tests
  }

  @Test
  public void testCreateLocalGroup() {
    // Arrange
    Riskrieg api = RiskriegBuilder.createLocal(Path.of("res/")).build();

    // Act
    Group group = api.createGroup(GroupIdentifier.of("test-group")).complete();

    // Assert
    assertTrue(Files.exists(Path.of("res/saves/test-group/")));
    assertNotNull(group);
    assertEquals(GroupIdentifier.of("test-group"), group.identifier());

    assertTrue(cleanup(group));
  }

  @Test
  public void testCreateLocalGame() {
    // Arrange
    Riskrieg api = RiskriegBuilder.createLocal(Path.of("res/")).build();
    Group group = api.createGroup(GroupIdentifier.of("test-group")).complete();

    // Act
    Game game = group.createGame(GameIdentifier.of("test-game"), Conquest.class).complete();

    // Assert
    assertTrue(Files.exists(Path.of("res/saves/test-group/test-game.json")));

    assertEquals(GameIdentifier.of("test-game"), game.identifier());
    assertEquals(GameConstants.standard(), game.constants());
    assertEquals(ColorPalette.standard(), game.palette());
    assertEquals(GamePhase.SETUP, game.phase());

    assertNotNull(game.creationTime());
    assertNotNull(game.updatedTime());
    assertNotNull(game.players());
    assertNotNull(game.nations());
    assertNotNull(game.claims());

    assertNull(game.map());

    assertEquals(0, game.players().size());
    assertEquals(0, game.nations().size());
    assertEquals(0, game.claims().size());

    assertTrue(cleanup(group, game));
  }

  @Test
  public void testSavingGame() throws IOException {
    // Arrange
    Group group = createLocalTestGroup("test-group");
    Game game = createLocalTestGame(group, "test-game", Mock.class);

    Path saveGamePath = Path.of("res/saves/test-group/test-game.json");
    Files.deleteIfExists(saveGamePath); // Delete the file, so we can test saving independently of creation

    // Act
    group.saveGame(game).complete();

    // Assert
    assertTrue(Files.exists(saveGamePath));

    assertTrue(cleanup(group, game));
  }

  @Test
  public void testRetrieveSave() {
    // Arrange
    Group group = createLocalTestGroup("test-group");
    createLocalTestGame(group, "test-game", Mock.class);

    // Act
    Game game = group.retrieveGame(GameIdentifier.of("test-game")).complete();

    // Assert
    assertNotNull(game);
    assertEquals(GameIdentifier.of("test-game"), game.identifier());

    assertTrue(cleanup(group, game));
  }

  @Test
  public void testSetPalette() {
    // Arrange
    Group group = createLocalTestGroup("test-group");
    Game game = createLocalTestGame(group, "test-game", Mock.class);
    assertEquals(ColorPalette.standard(), game.palette());

    // Act
    assertThrowsExactly(NullPointerException.class, () -> game.setPalette(null).complete());
    assertDoesNotThrow(() -> game.setPalette(ColorPalette.original()).complete());

    // Assert
    assertEquals(ColorPalette.original(), game.palette());

    assertTrue(cleanup(group, game));
  }

  @Test
  public void testSelectMap() throws IOException, NoSuchAlgorithmException {
    // Arrange
    Group group = createLocalTestGroup("test-group");
    Game game = createLocalTestGame(group, "test-game", Mock.class);
    assertNull(game.map());
    assertEquals(GamePhase.SETUP, game.phase());

    GameMap map = new RkmDecoder().decode(new URL("https://github.com/Riskrieg/maps/raw/main/antarctica.rkm"));

    // Act
    assertThrowsExactly(NullPointerException.class, () -> game.selectMap(null).complete());
    assertDoesNotThrow(() -> game.selectMap(map).complete());

    // Assert
    assertNotNull(game.map());
    assertEquals("antarctica", Objects.requireNonNull(game.map()).codename());

    assertTrue(cleanup(group, game));
  }

  // Private helper methods

  private Group createLocalTestGroup(String groupId) {
    Riskrieg api = RiskriegBuilder.createLocal(Path.of("res/")).build();
    return api.createGroup(GroupIdentifier.of(groupId)).complete();
  }

  private Game createLocalTestGame(Group group, String gameId, Class<? extends Game> type) {
    return group.createGame(GameIdentifier.of(gameId), type).complete();
  }

  private boolean cleanup(Group group, Game game) {
    Path testGroup = Path.of("res/" + group.identifier().id() + "/");
    Path testGame = Path.of("res/" + group.identifier().id() + "/" + game.identifier().id() + ".json");
    try {
      Files.deleteIfExists(testGame);
      Files.deleteIfExists(testGroup);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  private boolean cleanup(Group group) {
    Path testGroup = Path.of("res/" + group.identifier().id() + "/");
    try {
      Files.deleteIfExists(testGroup);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

}
