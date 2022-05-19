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

package com.riskrieg.core.util;

import com.riskrieg.core.util.adapter.ColorAdapter;
import com.riskrieg.core.util.adapter.InstantAdapter;
import com.riskrieg.core.util.adapter.PointAdapter;
import com.riskrieg.core.util.adapter.UUIDAdapter;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class MoshiUtil {

  private MoshiUtil() {
  }

  private static final Moshi.Builder jsonAdapterBuilder = new Moshi.Builder()
      .add(new InstantAdapter())
      .add(new UUIDAdapter())
      .add(new PointAdapter())
      .add(new ColorAdapter());

  public static void register(PolymorphicJsonAdapterFactory<?> factory, PolymorphicJsonAdapterFactory<?>... factories) {
    jsonAdapterBuilder.add(factory);
    for (PolymorphicJsonAdapterFactory<?> f : factories) {
      jsonAdapterBuilder.add(f);
    }
  }

  private static Moshi jsonAdapter() {
    return jsonAdapterBuilder.build();
  }

  // Read

  @Nullable
  public static <T> T read(@NonNull Path path, @NonNull Class<T> type) throws IOException {
    if (Files.isRegularFile(path) && Files.isReadable(path)) {
      JsonAdapter<T> jsonAdapter = jsonAdapter().adapter(type);
      return jsonAdapter.fromJson(Files.readString(path));
    }
    return null;
  }

  @Nullable
  public static <T> T read(@NonNull Path path, @NonNull Type type) throws IOException {
    if (Files.isRegularFile(path) && Files.isReadable(path)) {
      JsonAdapter<T> jsonAdapter = jsonAdapter().adapter(type);
      return jsonAdapter.fromJson(Files.readString(path));
    }
    return null;
  }

  // Write

  public static <T> void write(@NonNull Path path, @NonNull Class<T> type, @NonNull T object) throws IOException {
    Files.createDirectories(path.getParent());
    Files.writeString(path, jsonAdapter().adapter(type).indent("  ").toJson(object), StandardCharsets.UTF_8);
  }

  public static <T> void write(@NonNull Path path, @NonNull Type type, @NonNull T object) throws IOException {
    Files.createDirectories(path.getParent());
    Files.writeString(path, jsonAdapter().adapter(type).indent("  ").toJson(object), StandardCharsets.UTF_8);
  }

}
