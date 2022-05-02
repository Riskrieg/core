package com.riskrieg.core.api;

import com.riskrieg.core.internal.LocalRiskrieg;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class RiskriegBuilder {

  private Path path;

  private RiskriegBuilder(Path path) {
    Objects.requireNonNull(path);
    if (!Files.isDirectory(path)) {
      throw new IllegalStateException("The path provided must be a directory/folder.");
    }
    this.path = path;
  }

  public static RiskriegBuilder createLocal(@NonNull Path path) {
    return new RiskriegBuilder(path);
  }

  private static RiskriegBuilder createRemote() { // TODO: Implement support for remote repositories accessible via a REST API.
    return null;
  }

  public Riskrieg build() { // This is set up the way it is in preparation for allowing remote repositories.
    if (path != null) {
      return new LocalRiskrieg(path);
    }
    return new LocalRiskrieg(null);
  }

}
