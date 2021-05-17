import static org.junit.jupiter.api.Assertions.assertEquals;

import com.riskrieg.core.api.Riskrieg;
import com.riskrieg.core.api.RiskriegBuilder;
import com.riskrieg.core.api.gamemode.GameState;
import com.riskrieg.core.api.gamemode.classic.ClassicMode;
import com.riskrieg.core.api.gamemode.conquest.ConquestMode;
import com.riskrieg.core.api.map.MapOptions;
import com.riskrieg.core.api.map.options.Availability;
import com.riskrieg.core.api.map.options.InterfaceAlignment;
import com.riskrieg.core.api.map.options.alignment.HorizontalAlignment;
import com.riskrieg.core.api.map.options.alignment.VerticalAlignment;
import com.riskrieg.core.api.order.FullRandomOrder;
import com.riskrieg.core.api.player.Identity;
import com.riskrieg.map.RkmMap;
import com.riskrieg.map.vertex.Territory;
import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

public class ApiTests {

  @Test
  public void testRiskrieg() {
    Riskrieg api = RiskriegBuilder.create(Path.of("res/saves/")).build();
    var optGroup = api.createGroup("test").complete(failure -> System.out.println("failure: " + failure.getMessage()));
    optGroup.ifPresent(group -> group.createGame("testing", ConquestMode.class).submit());

    optGroup.ifPresent(group -> System.out.println(group.retrieveGames().size()));
  }

  @Test
  public void testJoin() {
    ClassicMode game = new ClassicMode();
    game.join("Name", new Color(1, 1, 1));
    assertEquals(0, game.players().size());
    game.join("Name", new Color(1, 1, 1)).submit();
    assertEquals(1, game.players().size());
    game.join("Name2", new Color(1, 1, 1)).submit();
    assertEquals(1, game.players().size());
    game.join(Identity.of("identity2"), "Name", new Color(1, 1, 1)).submit();
    assertEquals(1, game.players().size());
    game.join(Identity.of("identity2"), "Name", new Color(2, 2, 2)).submit();
    assertEquals(2, game.players().size());
    game.join(Identity.of("identity2"), "Name", new Color(3, 3, 3)).submit();
    assertEquals(2, game.players().size());
    game.join(Identity.of("identity3"), "Name", new Color(3, 3, 3)).submit();
    assertEquals(3, game.players().size());
  }

  @Test
  public void testConquest() {
    ClassicMode game = new ClassicMode();

    game.join(Identity.of("1234"), "Test", new Color(0, 0, 0));
    assertEquals(0, game.players().size());

    game.join(Identity.of("1234"), "Test", new Color(0, 0, 0)).submit();
    assertEquals(1, game.players().size());

    game.join(Identity.of("1234"), "Test", new Color(0, 0, 0)).submit();
    assertEquals(1, game.players().size());

    game.start(new FullRandomOrder()).submit();
    assertEquals(game.gameState(), GameState.SETUP);

//    assertFalse(game.map().isSet());

    try {
      game.selectMap(
          RkmMap.load(new URL("https://github.com/Riskrieg/maps/raw/main/antarctica.rkm")).orElseThrow(),
          new MapOptions(Availability.AVAILABLE, new InterfaceAlignment(VerticalAlignment.BOTTOM, HorizontalAlignment.LEFT)))
          .submit();
    } catch (IOException e) {
      e.printStackTrace();
    }
//    assertTrue(game.map().isSet());

    game.join("Janice", new Color(1, 1, 1)).submit(player -> {
      game.selectTerritory(player.identity(), game.map().graph().vertexSet().toArray(new Territory[0])[0].id()).submit();
    });
    assertEquals(2, game.players().size());
    assertEquals(1, game.nations().size());

    game.start(new FullRandomOrder()).submit();
    assertEquals(game.gameState(), GameState.SETUP);

    game.selectTerritory(Identity.of("1234"), game.map().graph().vertexSet().toArray(new Territory[0])[1].id()).submit();
    assertEquals(2, game.nations().size());

    game.start(new FullRandomOrder()).submit();
    assertEquals(game.gameState(), GameState.RUNNING);

  }

}
