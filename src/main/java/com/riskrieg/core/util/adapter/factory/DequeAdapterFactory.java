package com.riskrieg.core.util.adapter.factory;

import com.riskrieg.core.util.adapter.DequeAdapter;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Deque;
import java.util.Set;
import org.jetbrains.annotations.Nullable;

public class DequeAdapterFactory implements JsonAdapter.Factory {

  @Nullable
  @Override
  public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
    if (!annotations.isEmpty()) {
      return null;
    }

    if (!(type instanceof ParameterizedType parameterizedType)) {
      return null;
    }

    if (parameterizedType.getRawType() != Deque.class) {
      return null;
    }

    Type elementType = parameterizedType.getActualTypeArguments()[0];
    JsonAdapter<Object> elementAdapter = moshi.adapter(elementType);

    return new DequeAdapter<>(elementAdapter).nullSafe();
  }
}
