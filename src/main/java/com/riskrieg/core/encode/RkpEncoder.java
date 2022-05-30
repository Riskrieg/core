package com.riskrieg.core.encode;

import com.riskrieg.core.api.color.ColorPalette;
import com.riskrieg.core.util.io.MoshiUtil;
import java.io.IOException;
import java.io.OutputStream;

public final class RkpEncoder implements Encoder<ColorPalette> {

  @Override
  public void encode(ColorPalette object, OutputStream outputStream, boolean shouldCloseStream) throws IOException {
    MoshiUtil.write(outputStream, ColorPalette.class, object);
    if (shouldCloseStream) {
      outputStream.close();
    }
  }

}
