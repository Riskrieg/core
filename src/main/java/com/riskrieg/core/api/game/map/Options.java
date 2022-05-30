/*
 *     Riskrieg, an open-source conflict simulation game.
 *     Copyright (C) 2021 Aaron Yoder <aaronjyoder@gmail.com> and Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.riskrieg.core.api.game.map;

import com.riskrieg.core.api.game.map.options.Availability;
import com.riskrieg.core.api.game.map.options.Flavor;
import com.riskrieg.core.api.game.map.options.HorizontalAlignment;
import com.riskrieg.core.api.game.map.options.InterfaceAlignment;
import com.riskrieg.core.api.game.map.options.VerticalAlignment;
import com.riskrieg.core.util.io.RkJsonUtil;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.nio.file.Path;
import java.util.Objects;

public record Options(Flavor flavor, Availability availability, InterfaceAlignment alignment, boolean autogenTitle) {

  @NonNull
  public static Options load(@NonNull Path path) {
    try {
      Options result = RkJsonUtil.read(path, Options.class);
      if (result == null) {
        result = new Options();
        RkJsonUtil.write(path, Options.class, result);
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
