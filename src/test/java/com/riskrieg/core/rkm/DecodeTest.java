package com.riskrieg.core.rkm;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.riskrieg.core.api.game.map.GameMap;
import com.riskrieg.core.recode.decode.Decoder;
import com.riskrieg.core.recode.decode.RkmDecoder;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Test;

public class DecodeTest {

  private final Path testResourcePath = Paths.get("src", "test", "resources").toAbsolutePath();

  @Test
  public void testDecodeLocal() throws IOException, NoSuchAlgorithmException {
    Decoder<GameMap> decoder = new RkmDecoder();
    GameMap map = decoder.decode(testResourcePath.resolve(Path.of("maps", "antarctica.rkm")));
    assertDoesNotThrow(() -> {
      GameMap map2 = decoder.decode(testResourcePath.resolve(Path.of("maps", "antarctica.rkm")));
    });

    assertNotNull(map);
    assertNotNull(map.codename());
    assertNotNull(map.displayName());
    assertNotNull(map.author());
    assertNotNull(map.vertices());
    assertNotNull(map.edges());
    assertNotNull(map.baseLayer());
    assertNotNull(map.textLayer());

    assertEquals(map.codename(), "antarctica");
    assertEquals(map.displayName(), "Antarctica");
    assertEquals(map.author(), "Vidcom");
    assertEquals(map.vertices().size(), 89);
    assertEquals(map.edges().size(), 197);
  }

  @Test
  public void testDecodeRemote() throws IOException, NoSuchAlgorithmException {
    RkmDecoder decoder = new RkmDecoder();
    GameMap map = decoder.decode(new URL("https://github.com/Riskrieg/maps/raw/main/antarctica.rkm"));

    assertNotNull(map);
    assertNotNull(map.codename());
    assertNotNull(map.displayName());
    assertNotNull(map.author());
    assertNotNull(map.vertices());
    assertNotNull(map.edges());
    assertNotNull(map.baseLayer());
    assertNotNull(map.textLayer());

    assertEquals(map.codename(), "antarctica");
    assertEquals(map.displayName(), "Antarctica");
    assertEquals(map.author(), "Vidcom");
    assertEquals(map.vertices().size(), 89);
    assertEquals(map.edges().size(), 197);
  }

}
