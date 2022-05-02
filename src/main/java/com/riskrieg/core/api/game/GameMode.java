package com.riskrieg.core.api.game;

import edu.umd.cs.findbugs.annotations.NonNull;

public interface GameMode { // TODO: Figure out if this is even necessary at all.

  @NonNull
  String displayName();

  @NonNull
  String description();

}
