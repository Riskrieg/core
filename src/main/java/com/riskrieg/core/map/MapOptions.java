package com.riskrieg.core.map;

import com.aaronjyoder.util.json.gson.GsonUtil;
import com.riskrieg.core.constant.Constants;
import com.riskrieg.core.map.options.Availability;
import com.riskrieg.core.map.options.InterfaceAlignment;
import com.riskrieg.core.map.options.alignment.HorizontalAlignment;
import com.riskrieg.core.map.options.alignment.VerticalAlignment;
import com.riskrieg.map.data.MapName;
import java.nio.file.Path;

public final class MapOptions {

  private Availability availability;
  private InterfaceAlignment alignment;

  public static MapOptions load(String mapCodeName, boolean createIfUnavailable) {
    MapOptions result = GsonUtil.read(Constants.MAP_OPTIONS_PATH + mapCodeName + ".json", MapOptions.class);
    if (result == null && createIfUnavailable) {
      result = new MapOptions();
      GsonUtil.write(Constants.MAP_OPTIONS_PATH, mapCodeName + ".json", MapOptions.class, result);
    }
    return result;
  }

  public MapOptions() {
    this.availability = Availability.UNAVAILABLE;
    this.alignment = new InterfaceAlignment(VerticalAlignment.BOTTOM, HorizontalAlignment.LEFT);
  }

  public MapOptions(Availability availability, InterfaceAlignment alignment) {
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
