/*
 *     Riskrieg, an open-source conflict simulation game.
 *     Copyright (C) 2021 Aaron Yoder <aaronjyoder@gmail.com> and Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.riskrieg.core.util.adapter;

import com.riskrieg.core.util.adapter.json.PointJson;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;
import java.awt.Point;

public final class PointAdapter {

  @ToJson
  PointJson toJson(Point point) {
    return new PointJson(point.x, point.y);
  }

  @FromJson
  Point fromJson(PointJson pointJson) {
    return new Point(pointJson.x(), pointJson.y());
  }

}
