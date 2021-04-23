import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.riskrieg.core.api.Conquest;
import com.riskrieg.core.gamemode.GameState;
import com.riskrieg.core.order.RandomOrder;
import com.riskrieg.core.player.Identity;
import com.riskrieg.map.RkmMap;
import com.riskrieg.map.vertex.Territory;
import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.Test;

public class ApiTests {

  @Test
  public void testConquest() {
    Conquest game = new Conquest();

    game.join(Identity.of("1234"), "Test", new Color(0, 0, 0));
    assertEquals(0, game.players().size());

    game.join(Identity.of("1234"), "Test", new Color(0, 0, 0)).submit();
    assertEquals(1, game.players().size());

    game.join(Identity.of("1234"), "Test", new Color(0, 0, 0)).submit();
    assertEquals(1, game.players().size());

    game.start(new RandomOrder()).submit();
    assertEquals(game.gameState(), GameState.SETUP);

    assertFalse(game.map().isSet());

    try {
      game.selectMap(RkmMap.load(new URL("https://github.com/Riskrieg/core/raw/v2/res/maps/antarctica.rkm")).orElseThrow()).submit();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    assertTrue(game.map().isSet());

    game.join("Janice", new Color(1, 1, 1)).submit(player -> {
      game.formNation(player, game.map().getGraph().vertices().toArray(new Territory[0])[0].id()).submit();
    });
    assertEquals(2, game.players().size());
    assertEquals(1, game.nations().size());

    game.start(new RandomOrder()).submit();
    assertEquals(game.gameState(), GameState.SETUP);

    game.formNation(Identity.of("1234"), game.map().getGraph().vertices().toArray(new Territory[0])[1].id()).submit();
    assertEquals(2, game.nations().size());

    game.start(new RandomOrder()).submit();
    assertEquals(game.gameState(), GameState.RUNNING);

  }

}
