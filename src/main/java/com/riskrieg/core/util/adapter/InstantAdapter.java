package com.riskrieg.core.util.adapter;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;
import java.time.Instant;

public class InstantAdapter {


  @ToJson
  InstantJson toJson(Instant instant) {
    return new InstantJson(instant.getEpochSecond(), instant.getNano());
  }

  @FromJson
  Instant fromJson(InstantJson instantJson) {
    return Instant.ofEpochSecond(instantJson.seconds(), instantJson.nanos());
  }

}

record InstantJson(long seconds, long nanos) {

}