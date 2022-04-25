package com.riskrieg.core.util.adapter;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;
import java.awt.Color;

public class ColorAdapter {


  @ToJson
  Integer toJson(Color rgb) {
    return rgb.getRGB();
  }

  @FromJson
  Color fromJson(Integer rgb) {
    return new Color(rgb);
  }

}
