import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.riskrieg.core.api.Riskrieg;
import com.riskrieg.core.api.RiskriegBuilder;
import com.riskrieg.core.api.gamemode.AlliableMode;
import com.riskrieg.core.api.gamemode.GameState;
import com.riskrieg.core.api.gamemode.classic.ClassicMode;
import com.riskrieg.core.api.gamemode.conquest.ConquestMode;
import com.riskrieg.core.api.map.MapOptions;
import com.riskrieg.core.api.map.options.Availability;
import com.riskrieg.core.api.map.options.Flavor;
import com.riskrieg.core.api.map.options.InterfaceAlignment;
import com.riskrieg.core.api.map.options.alignment.HorizontalAlignment;
import com.riskrieg.core.api.map.options.alignment.VerticalAlignment;
import com.riskrieg.core.api.order.FullRandomOrder;
import com.riskrieg.core.api.order.StandardOrder;
import com.riskrieg.core.api.player.Identity;
import com.riskrieg.core.constant.Constants;
import com.riskrieg.core.constant.color.ColorId;
import com.riskrieg.map.RkmMap;
import com.riskrieg.map.territory.TerritoryId;
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
    game.join("Name", ColorId.of(0));
    assertEquals(0, game.players().size());
    game.join("Name", ColorId.of(0)).submit();
    assertEquals(1, game.players().size());
    game.join("Name2", ColorId.of(0)).submit();
    assertEquals(1, game.players().size());
    game.join(Identity.of("identity2"), "Name", ColorId.of(0)).submit();
    assertEquals(1, game.players().size());
    game.join(Identity.of("identity2"), "Name", ColorId.of(1)).submit();
    assertEquals(2, game.players().size());
    game.join(Identity.of("identity2"), "Name", ColorId.of(2)).submit();
    assertEquals(2, game.players().size());
    game.join(Identity.of("identity3"), "Name", ColorId.of(2)).submit();
    assertEquals(3, game.players().size());
  }

  @Test
  public void testAlliances() {
    AlliableMode game = new ConquestMode();

    game.join(Identity.of("1"), "Player1", ColorId.of(0)).submit();

    game.join(Identity.of("2"), "Player2", ColorId.of(1)).submit();

    game.join(Identity.of("3"), "Player3", ColorId.of(2)).submit();

    try {
      game.selectMap(
          RkmMap.load(new URL("https://github.com/Riskrieg/maps/raw/main/antarctica.rkm")).orElseThrow(),
          new MapOptions(Flavor.OFFICIAL, Availability.AVAILABLE, new InterfaceAlignment(VerticalAlignment.BOTTOM, HorizontalAlignment.LEFT)))
          .submit();
    } catch (IOException e) {
      e.printStackTrace();
    }

    game.selectTerritory(Identity.of("1"), new TerritoryId("1A")).submit();
    game.selectTerritory(Identity.of("2"), new TerritoryId("2A")).submit();
    game.selectTerritory(Identity.of("3"), new TerritoryId("3A")).submit();

    game.start(new StandardOrder()).submit();
    assertEquals(game.gameState(), GameState.RUNNING);

    game.ally(Identity.of("1"), Identity.of("2")).submit();
    assertFalse(game.allied(Identity.of("1"), Identity.of("2")));

    game.ally(Identity.of("2"), Identity.of("1")).submit();
    assertTrue(game.allied(Identity.of("1"), Identity.of("2")));

    game.unally(Identity.of("1"), Identity.of("2")).submit();
    assertFalse(game.allied(Identity.of("1"), Identity.of("2")));

  }

  @Test
  public void testClassic() {
    ClassicMode game = new ClassicMode();

    game.join(Identity.of("1234"), "Test", ColorId.of(3));
    assertEquals(0, game.players().size());

    game.join(Identity.of("1234"), "Test", ColorId.of(3)).submit();
    assertEquals(1, game.players().size());

    game.join(Identity.of("1234"), "Test", ColorId.of(3)).submit();
    assertEquals(1, game.players().size());

    game.start(new FullRandomOrder()).submit();
    assertEquals(game.gameState(), GameState.SETUP);

//    assertFalse(game.map().isSet());

    try {
      game.selectMap(
          RkmMap.load(new URL("https://github.com/Riskrieg/maps/raw/main/antarctica.rkm")).orElseThrow(),
          new MapOptions(Flavor.OFFICIAL, Availability.AVAILABLE, new InterfaceAlignment(VerticalAlignment.BOTTOM, HorizontalAlignment.LEFT)))
          .submit();
    } catch (IOException e) {
      e.printStackTrace();
    }
//    assertTrue(game.map().isSet());

    game.join("Janice", ColorId.of(4)).submit(player -> {
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
