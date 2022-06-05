package com.riskrieg.core.rkm;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.riskrieg.codec.decode.Decoder;
import com.riskrieg.codec.decode.RkmDecoder;
import com.riskrieg.codec.encode.Encoder;
import com.riskrieg.codec.encode.RkmEncoder;
import com.riskrieg.map.RkmMap;
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
    RkmMap map = loadLocal("antarctica");
    Encoder<RkmMap> encoder = new RkmEncoder();

    Path testMapPath = testResourcePath.resolve(Path.of("maps", "test.rkm"));
    assertTrue(Files.notExists(testMapPath));

    encoder.encode(map, new FileOutputStream(testMapPath.toFile()));
    assertTrue(Files.exists(testMapPath));

    Files.deleteIfExists(testMapPath);
    assertTrue(Files.notExists(testMapPath));
  }

  private RkmMap loadLocal(String codename) throws IOException, NoSuchAlgorithmException {
    Decoder<RkmMap> decoder = new RkmDecoder();
    return decoder.decode(testResourcePath.resolve(Path.of("maps", codename + ".rkm")));
  }

}
