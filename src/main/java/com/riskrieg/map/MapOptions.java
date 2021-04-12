package com.riskrieg.map;

import com.aaronjyoder.util.json.gson.GsonUtil;
import com.riskrieg.constant.Constants;
import com.riskrieg.map.data.MapName;
import com.riskrieg.map.options.Availability;
import com.riskrieg.map.options.InterfaceAlignment;
import com.riskrieg.map.options.alignment.HorizontalAlignment;
import com.riskrieg.map.options.alignment.VerticalAlignment;

public class MapOptions {

  private Availability availability;
  private InterfaceAlignment alignment;

  public static MapOptions load(MapName mapName) {
    return GsonUtil.read(Constants.MAP_OPTIONS_PATH + mapName.name() + ".json", MapOptions.class);
  }

  public MapOptions() {
    this.availability = Availability.AVAILABLE;
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
