import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.riskrieg.api.Riskrieg;
import com.riskrieg.constant.Constants;
import com.riskrieg.gamemode.Game;
import com.riskrieg.gamemode.GameMode;
import com.riskrieg.gamemode.conquest.Conquest;
import com.riskrieg.gamemode.creative.Creative;
import com.riskrieg.map.GameMap;
import com.riskrieg.player.ComputerPlayer;
import com.riskrieg.player.HumanPlayer;
import com.riskrieg.player.Player;
import com.riskrieg.player.PlayerColor;
import java.io.File;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class ConquestTest {

  @Test
  public void testApi() {
    Riskrieg api = new Riskrieg();

    /*
     * Riskrieg#create cases
     */
    assertFalse(api.create(null, null, null).success());
    assertFalse(api.create(null, "", null).success());
    assertFalse(api.create(null, null, "").success());
    assertFalse(api.create(GameMode.CONQUEST, null, null).success());
    assertFalse(api.create(GameMode.CONQUEST, "", null).success());
    assertFalse(api.create(GameMode.CONQUEST, null, "").success());

    assertTrue(api.create(GameMode.CONQUEST, "test", "conquestTest").success());
    assertFalse(api.create(GameMode.CONQUEST, "test", "conquestTest").success());

    assertTrue(api.create(GameMode.CREATIVE, "test", "creativeTest").success());

    assertTrue(new File(Constants.SAVE_PATH + "test" + "/").exists());
    assertTrue(new File(Constants.SAVE_PATH + "test" + "/" + "conquestTest.json").exists());
    assertTrue(new File(Constants.SAVE_PATH + "test" + "/" + "creativeTest.json").exists());


    /*
     * Riskrieg#load cases
     */
    assertTrue(api.load(null, null).isEmpty());
    assertTrue(api.load("", null).isEmpty());
    assertTrue(api.load(null, "").isEmpty());
    assertTrue(api.load("", "").isEmpty());
    assertTrue(api.load("invalid", "invalid").isEmpty());

    Optional<Game> optConquest = api.load("test", "conquestTest");
    assertTrue(optConquest.isPresent());
    assertTrue(optConquest.get() instanceof Conquest);

    Optional<Game> optCreative = api.load("test", "creativeTest");
    assertTrue(optCreative.isPresent());
    assertTrue(optCreative.get() instanceof Creative);

    /*
     * Riskrieg#save cases
     */
    assertFalse(api.save(null, null, null).success());
    assertFalse(api.save(null, "invalid", null).success());
    assertFalse(api.save(null, null, "invalid").success());
    assertFalse(api.save(null, "invalid", "invalid").success());
    assertFalse(api.save(null, "", null).success());
    assertFalse(api.save(null, null, "").success());
    assertFalse(api.save(null, "", "").success());
    assertFalse(api.save(optConquest.get(), null, null).success());
    assertFalse(api.save(optConquest.get(), "", null).success());
    assertFalse(api.save(optConquest.get(), null, "").success());
    assertFalse(api.save(optConquest.get(), "", "").success());

    assertTrue(api.save(optConquest.get(), "test", "conquestTest").success());
    assertTrue(api.save(optCreative.get(), "test", "creativeTest").success());

    assertTrue(new File(Constants.SAVE_PATH + "test" + "/").exists());
    assertTrue(new File(Constants.SAVE_PATH + "test" + "/" + "conquestTest.json").exists());
    assertTrue(new File(Constants.SAVE_PATH + "test" + "/" + "creativeTest.json").exists());

    api.save(optConquest.get(), "test");

    /*
     * Riskrieg#loadSaves cases
     */
    assertTrue(api.loadSaves(null).isEmpty());
    assertTrue(api.loadSaves("").isEmpty());
    assertTrue(api.loadSaves("invalid").isEmpty());
    assertEquals(api.loadSaves("test").size(), 2);

    /*
     * Riskrieg#delete cases
     */
    assertFalse(api.delete(null, null).success());
    assertFalse(api.delete("invalid", null).success());
    assertFalse(api.delete(null, "invalid").success());
    assertFalse(api.delete("", null).success());
    assertFalse(api.delete(null, "").success());
    assertFalse(api.delete("", "").success());
    assertFalse(api.delete("invalid", "invalid").success());

    assertTrue(api.delete("test", "conquestTest").success());
    assertTrue(api.delete("test", "creativeTest").success());

    assertFalse(new File(Constants.SAVE_PATH + "test" + "/").exists());
    assertFalse(new File(Constants.SAVE_PATH + "test" + "/" + "conquestTest.json").exists());
    assertFalse(new File(Constants.SAVE_PATH + "test" + "/" + "creativeTest.json").exists());
  }

  @Test
  public void testConstructors() {
    assertThrows(NullPointerException.class, () -> new HumanPlayer(null, null, null));
    assertThrows(NullPointerException.class, () -> new HumanPlayer("", null, null));
    assertThrows(NullPointerException.class, () -> new HumanPlayer(null, "", null));
    assertThrows(NullPointerException.class, () -> new HumanPlayer("", "", null));
    assertThrows(NullPointerException.class, () -> new HumanPlayer(null, null, PlayerColor.CADMIUM));
    assertThrows(IllegalArgumentException.class, () -> new HumanPlayer("", null, PlayerColor.CADMIUM));
    assertThrows(NullPointerException.class, () -> new HumanPlayer(null, "", PlayerColor.CADMIUM));
    assertThrows(IllegalArgumentException.class, () -> new HumanPlayer("", "", PlayerColor.CADMIUM));

    assertThrows(NullPointerException.class, () -> new HumanPlayer(null, null));
    assertThrows(NullPointerException.class, () -> new HumanPlayer("", null));
    assertThrows(NullPointerException.class, () -> new HumanPlayer(null, PlayerColor.CADMIUM));
    assertThrows(IllegalArgumentException.class, () -> new HumanPlayer("", PlayerColor.CADMIUM));

    assertThrows(NullPointerException.class, () -> new ComputerPlayer(null, null));
    assertThrows(NullPointerException.class, () -> new ComputerPlayer("", null));
    assertThrows(NullPointerException.class, () -> new ComputerPlayer(null, PlayerColor.CADMIUM));
    assertThrows(IllegalArgumentException.class, () -> new ComputerPlayer("", PlayerColor.CADMIUM));

    assertThrows(NullPointerException.class, () -> new GameMap(null));
    assertThrows(IllegalArgumentException.class, () -> new GameMap(""));
    assertThrows(IllegalArgumentException.class, () -> new GameMap("invalid"));
  }

  @Test
  public void testGameNulls() {
    Game game = new Conquest();

    /*
     * Null/invalid cases and other null checks
     */
    assertNotNull(game.getCreationTime());
    assertNotNull(game.getLastUpdated());

    assertNotNull(game.getName());
    assertFalse(game.getName().isEmpty());

    assertNotNull(game.getDescription());
    assertFalse(game.getDescription().isEmpty());

    assertFalse(game.isEnded());

    assertTrue(game.getMap().isEmpty());

    assertTrue(game.getGameRule(null).isEmpty());
    assertTrue(game.getGameRule(null).isEmpty());

    assertFalse(game.start().success());
    assertTrue(game.getGameRule(null).isEmpty());
    assertTrue(game.getGameRule("").isEmpty());

    assertTrue(game.getGameRule("invalid").isEmpty());

    assertTrue(game.getPlayer((String) null).isEmpty());
    assertTrue(game.getPlayer("invalid").isEmpty());
    assertTrue(game.getPlayer((PlayerColor) null).isEmpty());

    assertTrue(game.getPlayer(PlayerColor.CADMIUM).isEmpty());

    assertFalse(game.updatePlayerName((Player) null, null).success());
    assertFalse(game.updatePlayerName((String) null, null).success());
    assertFalse(game.updatePlayerName((PlayerColor) null, null).success());

    assertTrue(game.getNation((Player) null).isEmpty());
    assertTrue(game.getNation((String) null).isEmpty());
    assertTrue(game.getNation((PlayerColor) null).isEmpty());

    assertFalse(game.setGameRule(null, false).success());

    assertFalse(game.setMap(null).success());

    assertFalse(game.join(null).success());

    assertFalse(game.kick((Player) null).success());
    assertFalse(game.kick((String) null).success());
    assertFalse(game.kick((PlayerColor) null).success());

    assertFalse(game.formNation((Player) null, null).success());
    assertFalse(game.formNation((String) null, null).success());
    assertFalse(game.formNation((PlayerColor) null, null).success());

    assertFalse(game.dissolveNation(null).success());

    assertFalse(game.start().success());

    assertFalse(game.turn().success());

    assertFalse(game.skip((Player) null).success());
    assertFalse(game.skip((PlayerColor) null).success());

    assertFalse(game.claim((Player) null).success());
    assertFalse(game.claim((String) null).success());
    assertFalse(game.claim((PlayerColor) null).success());

    assertTrue(game.update().success());
  }

  @Test
  public void testConquest() {
    Game game = new Conquest();
    assertTrue(game.join(new HumanPlayer("Test", PlayerColor.CADMIUM)).success());
    assertTrue(game.kick(PlayerColor.CADMIUM).success());
    assertFalse(game.getPlayer(PlayerColor.CADMIUM).isPresent());

  }

  @Test
  public void adjustRule() {
    Game game = new Conquest();
    assertTrue(game.getGameRule("alliances").isPresent());
    game.setGameRule("alliances", false);
    assertFalse(game.getGameRule("alliances").get().isEnabled());
    game.setGameRule("alliances", true);
    assertTrue(game.getGameRule("alliances").get().isEnabled());
  }

}