package com.riskrieg.core.api;

import com.riskrieg.core.internal.FileFolderRiskrieg;
import java.nio.file.Path;
import java.util.Objects;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public final class RiskriegBuilder {

  private final Path repository;

  private RiskriegBuilder(Path repository) {
    Objects.requireNonNull(repository);
    this.repository = repository;
  }

  @Nonnull
  @CheckReturnValue
  public static RiskriegBuilder create(@Nonnull Path repository) {
    return new RiskriegBuilder(repository);
  }

  public Riskrieg build() {
    return new FileFolderRiskrieg(repository);
  }

}
