package com.riskrieg.core.decode;

import com.riskrieg.core.api.color.ColorPalette;
import com.riskrieg.core.util.io.RkJsonUtil;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;

public final class RkpDecoder implements Decoder<ColorPalette> {

  @Override
  public ColorPalette decode(Path path) throws IOException {
    Objects.requireNonNull(path);
    return RkJsonUtil.read(path, ColorPalette.class);
  }

  @Override
  public ColorPalette decode(URL url) throws IOException {
    Objects.requireNonNull(url);
    try (InputStream inputStream = url.openStream()) {
      final byte[] data = inputStream.readAllBytes();
      return RkJsonUtil.read(new String(data, StandardCharsets.UTF_8), ColorPalette.class);
    }
  }

  @Override
  public ColorPalette decode(byte[] data) throws IOException {
    Objects.requireNonNull(data);
    return RkJsonUtil.read(new String(data, StandardCharsets.UTF_8), ColorPalette.class);
  }

}
