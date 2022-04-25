package com.riskrieg.core.util;

import com.riskrieg.core.util.adapter.ColorAdapter;
import com.riskrieg.core.util.adapter.InstantAdapter;
import com.riskrieg.core.util.adapter.PointAdapter;
import com.riskrieg.core.util.adapter.UUIDAdapter;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
  public static <T> T read(@Nonnull Path path, @Nonnull Class<T> type) throws IOException {
    if (Files.isRegularFile(path) && Files.isReadable(path)) {
      JsonAdapter<T> jsonAdapter = jsonAdapter().adapter(type);
      return jsonAdapter.fromJson(Files.readString(path));
    }
    return null;
  }

  @Nullable
  public static <T> T read(@Nonnull Path path, @Nonnull Type type) throws IOException {
    if (Files.isRegularFile(path) && Files.isReadable(path)) {
      JsonAdapter<T> jsonAdapter = jsonAdapter().adapter(type);
      return jsonAdapter.fromJson(Files.readString(path));
    }
    return null;
  }

  // Write

  public static <T> void write(@Nonnull Path path, @Nonnull Class<T> type, @Nonnull T object) throws IOException {
    Files.createDirectories(path.getParent());
    Files.writeString(path, jsonAdapter().adapter(type).indent("  ").toJson(object), StandardCharsets.UTF_8);
  }

  public static <T> void write(@Nonnull Path path, @Nonnull Type type, @Nonnull T object) throws IOException {
    Files.createDirectories(path.getParent());
    Files.writeString(path, jsonAdapter().adapter(type).indent("  ").toJson(object), StandardCharsets.UTF_8);
  }

}
