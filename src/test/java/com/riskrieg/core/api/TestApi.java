package com.riskrieg.core.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.riskrieg.core.api.color.ColorBatch;
import com.riskrieg.core.api.game.Game;
import com.riskrieg.core.api.game.entity.nation.Nation;
import com.riskrieg.core.api.game.mode.Conquest;
import com.riskrieg.core.api.game.territory.GameTerritory;
import com.riskrieg.core.api.game.territory.TerritoryType;
import com.riskrieg.core.api.group.Group;
import com.riskrieg.core.api.identifier.GameIdentifier;
import com.riskrieg.core.api.identifier.GroupIdentifier;
import com.riskrieg.core.api.identifier.PlayerIdentifier;
import com.riskrieg.core.api.identifier.TerritoryIdentifier;
import com.riskrieg.core.decode.RkmDecoder;
import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import org.junit.jupiter.api.Test;

public class TestApi { // TODO: Implement comprehensive tests

  @Test
  public void testRepositoryOperations() {
    Riskrieg api = RiskriegBuilder.createLocal(Path.of("res/")).build();
    Collection<Group> groups = api.retrieveAllGroups().complete();

    Group group = api.createGroup(GroupIdentifier.of("12345")).complete();
    Game game = group.createGame(GameIdentifier.of("123"), Conquest.class).complete();

    var batch = ColorBatch.standard();
    for (int i = 0; i < batch.set().size(); i++) {
      assertEquals(i, batch.get(i).id());
    }
    assertEquals(0, ColorBatch.standard().get(-1).id());
    assertEquals(15, ColorBatch.standard().get(16).id());

    game.addPlayer(PlayerIdentifier.of("1"), "One").complete();
    Nation nation = game.createNation(ColorBatch.standard().get(3), PlayerIdentifier.of("1")).complete();

    RkmDecoder decoder = new RkmDecoder();

    try {
      game.selectMap(decoder.decode(Path.of("res/maps/antarctica.rkm"))).complete();
    } catch (IOException | NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }

    game.claim(nation.identifier(), GameTerritory.of(TerritoryIdentifier.of("1F"), TerritoryType.CAPITAL)).complete();

    group.saveGame(game).complete();

    Game testGame = group.retrieveGame(GameIdentifier.of("123")).complete();
    assertNotNull(testGame);
    assertEquals("123", testGame.identifier().id());
    assertEquals(16, testGame.colors().size());
    assertTrue(testGame instanceof Conquest);

    group.deleteGame(GameIdentifier.of("123")).complete();


    /*
     * /create mode opt:colors
     * Game objects should define their own list of valid game states, in addition to the base states required of all games.
     */

  }

  public void createLocalGroup() {
    Riskrieg api = RiskriegBuilder.createLocal(Path.of("res/saves/")).build();
    Group group = api.createGroup(GroupIdentifier.of("12345")).complete();
  }

  public void createLocalGame() {
    Riskrieg api = RiskriegBuilder.createLocal(Path.of("res/saves/")).build();
    Group group = api.createGroup(GroupIdentifier.of("12345")).complete();

    Game game = group.createGame(GameIdentifier.of("123"), Conquest.class).complete();
  }

}
