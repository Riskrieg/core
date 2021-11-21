/**
 *     Riskrieg, an open-source conflict simulation game.
 *     Copyright (C) 2021  Aaron Yoder <aaronjyoder@gmail.com>
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

package com.riskrieg.core.api.map;

import com.aaronjyoder.util.json.gson.GsonUtil;
import com.riskrieg.core.api.map.options.Availability;
import com.riskrieg.core.api.map.options.Flavor;
import com.riskrieg.core.api.map.options.InterfaceAlignment;
import com.riskrieg.core.api.map.options.alignment.HorizontalAlignment;
import com.riskrieg.core.api.map.options.alignment.VerticalAlignment;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nonnull;

public final class MapOptions {

  private Flavor flavor;
  private Availability availability;
  private InterfaceAlignment alignment;
  private boolean autogenTitle;

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
    this.flavor = Flavor.UNKNOWN;
    this.availability = Availability.UNAVAILABLE;
    this.alignment = new InterfaceAlignment(VerticalAlignment.BOTTOM, HorizontalAlignment.LEFT);
    this.autogenTitle = true;
  }

  public MapOptions(Flavor flavor, Availability availability, InterfaceAlignment alignment) {
    Objects.requireNonNull(flavor);
    Objects.requireNonNull(availability);
    Objects.requireNonNull(alignment);
    this.flavor = flavor;
    this.availability = availability;
    this.alignment = alignment;
    this.autogenTitle = true;
  }

  public Flavor flavor() {
    return flavor;
  }

  public InterfaceAlignment alignment() {
    return alignment;
  }

  public Availability availability() {
    return availability;
  }

  public boolean autogenTitle() {
    return autogenTitle;
  }

  public void setFlavor(Flavor flavor) {
    Objects.requireNonNull(flavor);
    this.flavor = flavor;
  }

  public void setAlignment(InterfaceAlignment alignment) {
    Objects.requireNonNull(alignment);
    this.alignment = alignment;
  }

  public void setAvailability(Availability availability) {
    Objects.requireNonNull(availability);
    this.availability = availability;
  }

  public void setAutogenTitle(boolean autogenTitle) {
    this.autogenTitle = autogenTitle;
  }

}
