package com.riskrieg.core.api.game.map;

import com.riskrieg.core.api.game.map.options.Availability;
import com.riskrieg.core.api.game.map.options.Flavor;
import com.riskrieg.core.api.game.map.options.HorizontalAlignment;
import com.riskrieg.core.api.game.map.options.InterfaceAlignment;
import com.riskrieg.core.api.game.map.options.VerticalAlignment;
import com.riskrieg.core.util.MoshiUtil;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.nio.file.Path;
import java.util.Objects;

public record Options(Flavor flavor, Availability availability, InterfaceAlignment alignment, boolean autogenTitle) {

  @NonNull
  public static Options load(@NonNull Path path) {
    try {
      Options result = MoshiUtil.read(path, Options.class);
      if (result == null) {
        result = new Options();
        MoshiUtil.write(path, Options.class, result);
      }
      return result;
    } catch (Exception e) {
      return new Options();
    }
  }

  public Options {
    Objects.requireNonNull(flavor);
    Objects.requireNonNull(availability);
    Objects.requireNonNull(alignment);
  }

  public Options() {
    this(Flavor.UNKNOWN, Availability.UNAVAILABLE, new InterfaceAlignment(VerticalAlignment.BOTTOM, HorizontalAlignment.LEFT), true);
  }

  public Options(Flavor flavor, Availability availability, InterfaceAlignment alignment) {
    this(flavor, availability, alignment, true);
  }

  public Options withFlavor(Flavor newFlavor) {
    return new Options(newFlavor, availability, alignment, autogenTitle);
  }

  public Options withAvailability(Availability newAvailability) {
    return new Options(flavor, newAvailability, alignment, autogenTitle);
  }

  public Options withAlignment(InterfaceAlignment newAlignment) {
    return new Options(flavor, availability, newAlignment, autogenTitle);
  }

  public Options withAutogenTitle(boolean shouldAutogenTitle) {
    return new Options(flavor, availability, alignment, shouldAutogenTitle);
  }

}