package com.riskrieg.core.api.map;

import com.aaronjyoder.util.json.gson.GsonUtil;
import com.riskrieg.core.api.map.options.Availability;
import com.riskrieg.core.api.map.options.InterfaceAlignment;
import com.riskrieg.core.api.map.options.alignment.HorizontalAlignment;
import com.riskrieg.core.api.map.options.alignment.VerticalAlignment;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nonnull;

public final class MapOptions {

  private Availability availability;
  private InterfaceAlignment alignment;

  @Nonnull
  public static Optional<MapOptions> load(@Nonnull Path optionsPath, boolean createIfUnavailable) {
    try {
      MapOptions result = GsonUtil.read(optionsPath, MapOptions.class);
      if (result == null && createIfUnavailable) {
        result = new MapOptions();
        GsonUtil.write(optionsPath, MapOptions.class, result);
      }
      return Optional.ofNullable(result);
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  public MapOptions() {
    this.availability = Availability.UNAVAILABLE;
    this.alignment = new InterfaceAlignment(VerticalAlignment.BOTTOM, HorizontalAlignment.LEFT);
  }

  public MapOptions(Availability availability, InterfaceAlignment alignment) {
    Objects.requireNonNull(availability);
    Objects.requireNonNull(alignment);
    this.availability = availability;
    this.alignment = alignment;
  }

  public InterfaceAlignment alignment() {
    return alignment;
  }

  public Availability availability() {
    return availability;
  }

  public void setAlignment(InterfaceAlignment alignment) {
    this.alignment = alignment;
  }

  public void setAvailability(Availability availability) {
    this.availability = availability;
  }

}
