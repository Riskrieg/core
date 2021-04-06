package com.riskrieg.api.gamemap;

import java.io.File;
import java.net.URI;
import java.util.Objects;

// TODO: Implement Riskrieg Map (.rkm) file format for maps so that they can be easily loaded, saved, and shared as a single file.
public final class GameMap {

  public static GameMap load(URI uri) {
    Objects.requireNonNull(uri);
    File file = new File(uri);
    // TODO: Do safe checks to ensure this is a valid .rkm file. Should be done in such a way as to prevent malicious actors.
    // TODO: Implement method
    return null;
  }

}
