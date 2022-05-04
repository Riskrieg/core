package com.riskrieg.core.rkm;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.riskrieg.core.api.game.map.GameMap;
import com.riskrieg.core.recoder.Decoder;
import com.riskrieg.core.recoder.Encoder;
import com.riskrieg.core.rkm.recoder.RkmDecoder;
import com.riskrieg.core.rkm.recoder.RkmEncoder;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Test;

public class EncodeTest {

  private final Path testResourcePath = Paths.get("src", "test", "resources").toAbsolutePath();

  @Test
  public void encodeLocal() throws IOException, NoSuchAlgorithmException {
    GameMap map = loadLocal("antarctica");
    Encoder<GameMap> encoder = new RkmEncoder();

    Path testMapPath = testResourcePath.resolve(Path.of("maps", "test.rkm"));
    assertTrue(Files.notExists(testMapPath));

    encoder.encode(map, new FileOutputStream(testMapPath.toFile()));
    assertTrue(Files.exists(testMapPath));

    Files.deleteIfExists(testMapPath);
    assertTrue(Files.notExists(testMapPath));
  }

  private GameMap loadLocal(String codename) throws IOException, NoSuchAlgorithmException {
    Decoder<GameMap> decoder = new RkmDecoder();
    return decoder.decode(testResourcePath.resolve(Path.of("maps", codename + ".rkm")));
  }

}
