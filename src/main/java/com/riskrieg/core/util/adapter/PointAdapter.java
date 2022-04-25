package com.riskrieg.core.util.adapter;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;
import java.awt.Point;

public class PointAdapter {

  @ToJson
  PointJson toJson(Point point) {
    return new PointJson(point.x, point.y);
  }

  @FromJson
  Point fromJson(PointJson pointJson) {
    return new Point(pointJson.x(), pointJson.y());
  }

}

record PointJson(int x, int y) {

}
