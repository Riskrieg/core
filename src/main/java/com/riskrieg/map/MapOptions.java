package com.riskrieg.map;

import com.aaronjyoder.util.json.gson.GsonUtil;
import com.riskrieg.constant.Constants;
import com.riskrieg.map.data.MapName;
import com.riskrieg.map.options.Availability;
import com.riskrieg.map.options.InterfaceAlignment;
import com.riskrieg.map.options.alignment.HorizontalAlignment;
import com.riskrieg.map.options.alignment.VerticalAlignment;

public final class MapOptions {

  private Availability availability;
  private InterfaceAlignment alignment;

  public static MapOptions load(MapName mapName, boolean createIfUnavailable) {
    MapOptions result = GsonUtil.read(Constants.MAP_OPTIONS_PATH + mapName.name() + ".json", MapOptions.class);
    if (result == null && createIfUnavailable) {
      result = new MapOptions();
      GsonUtil.write(Constants.MAP_OPTIONS_PATH, mapName.name() + ".json", MapOptions.class, result);
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
