package com.riskrieg.core.util.adapter;

import com.riskrieg.core.api.game.Game;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

public class GameTypeAdapter {

  @ToJson
  String toJson(Class<? extends Game> type) {
    return type.getTypeName();
  }

  @FromJson
  Class<? extends Game> fromJson(String typeName) {
    try {
      return Class.forName(typeName).asSubclass(Game.class);
    } catch (ClassNotFoundException | ClassCastException e) {
      throw new RuntimeException("Invalid cast with game type class loaded from save file");
    }
  }

}
