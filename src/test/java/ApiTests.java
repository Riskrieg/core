import static org.junit.jupiter.api.Assertions.assertEquals;

import com.riskrieg.core.api.Conquest;
import com.riskrieg.core.player.Identity;
import java.awt.Color;
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

  }

}
