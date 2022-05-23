package com.riskrieg.core.util.adapter.factory;

import com.riskrieg.core.util.adapter.SortedSetAdapter;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.SortedSet;

public class SortedSetAdapterFactory implements JsonAdapter.Factory {

  @Override
  public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
    if (!annotations.isEmpty()) {
      return null;
    }

    if (!(type instanceof ParameterizedType parameterizedType)) {
      return null;
    }

    if (parameterizedType.getRawType() != SortedSet.class) {
      return null;
    }

    Type elementType = parameterizedType.getActualTypeArguments()[0];
    JsonAdapter<Object> elementAdapter = moshi.adapter(elementType);

    return new SortedSetAdapter<>(elementAdapter).nullSafe();
  }

}
