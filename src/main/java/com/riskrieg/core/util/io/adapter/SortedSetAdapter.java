package com.riskrieg.core.util.io.adapter;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;
import org.jetbrains.annotations.Nullable;

public final class SortedSetAdapter<T> extends JsonAdapter<SortedSet<T>> {

  private final JsonAdapter<T> elementAdapter;

  public SortedSetAdapter(JsonAdapter<T> elementAdapter) {
    this.elementAdapter = elementAdapter;
  }

  @Override
  public SortedSet<T> fromJson(JsonReader reader) throws IOException {
    TreeSet<T> result = new TreeSet<>();
    reader.beginArray();
    while (reader.hasNext()) {
      result.add(elementAdapter.fromJson(reader));
    }
    reader.endArray();
    return result;
  }

  @Override
  public void toJson(JsonWriter writer, @Nullable SortedSet<T> set) throws IOException {
    writer.beginArray();
    for (T element : set) {
      elementAdapter.toJson(writer, element);
    }
    writer.endArray();
  }

}
