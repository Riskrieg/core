package com.riskrieg.core.util.adapter;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;

public final class DequeAdapter<T> extends JsonAdapter<Deque<T>> {

  private final JsonAdapter<T> elementAdapter;

  public DequeAdapter(JsonAdapter<T> elementAdapter) {
    this.elementAdapter = elementAdapter;
  }

  @Override
  public Deque<T> fromJson(JsonReader reader) throws IOException {
    ArrayDeque<T> result = new ArrayDeque<>();
    reader.beginArray();
    while (reader.hasNext()) {
      result.add(Objects.requireNonNull(elementAdapter.fromJson(reader)));
    }
    reader.endArray();
    return result;
  }

  @Override
  public void toJson(JsonWriter writer, @Nullable Deque<T> set) throws IOException {
    writer.beginArray();
    for (T element : set) {
      elementAdapter.toJson(writer, element);
    }
    writer.endArray();
  }

}
