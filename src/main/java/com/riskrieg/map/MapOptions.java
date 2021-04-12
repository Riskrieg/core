package com.riskrieg.map;


import com.riskrieg.map.options.Availability;
import com.riskrieg.map.options.InterfaceAlignment;
import com.riskrieg.map.options.alignment.HorizontalAlignment;
import com.riskrieg.map.options.alignment.VerticalAlignment;

public class MapOptions {

  private Availability availability;
  private InterfaceAlignment alignment;

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
