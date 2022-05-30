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

package com.riskrieg.core.api.color;

import java.awt.Color;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public record ColorPalette(String name, SortedSet<GameColor> set) { // Encode as json with file extension of (.rkp)

  public static final int MINIMUM_SIZE = 2;
  public static final int MAXIMUM_SIZE = 16;

  public ColorPalette {
    Objects.requireNonNull(name);
    Objects.requireNonNull(set);
    if (name.isBlank()) {
      throw new IllegalStateException("String 'name' cannot be blank");
    }
    if (set.size() < MINIMUM_SIZE) {
      throw new IllegalStateException("Your color set cannot have fewer than " + MINIMUM_SIZE + " colors defined. You have " + set.size() + " unique items in your set.");
    } else if (set.size() > MAXIMUM_SIZE) {
      throw new IllegalStateException("Your color set cannot have more than " + MAXIMUM_SIZE + " colors defined. You have " + set.size() + " unique items in your set.");
    }
    set = Collections.unmodifiableSortedSet(set);
  }

  public ColorPalette(String name, GameColor... colors) {
    this(name, new TreeSet<>(Set.of(colors)));
  }

  public GameColor first() {
    return set.first();
  }

  public GameColor last() {
    return set.last();
  }

  public GameColor get(int index) {
    if (index < 0) {
      return first();
    } else if (index >= set.size()) {
      return last();
    }
    return set.toArray(GameColor[]::new)[index];
  }

  public GameColor valueOf(Color color) {
    Objects.requireNonNull(color);
    for (GameColor gameColor : set) {
      if (gameColor.toColor().equals(color)) {
        return gameColor;
      }
    }
    return last();
  }

  public int size() {
    return set.size();
  }

  /**
   * The current default palette. Includes support for most types of color blindness.
   *
   * @return the default color palette
   */
  public static ColorPalette standard() {
    return new ColorPalette("Default",
        new GameColor(0, "Salmon", 255, 140, 150), new GameColor(1, "Lavender", 155, 120, 190),
        new GameColor(2, "Thistle", 215, 190, 240), new GameColor(3, "Ice", 195, 230, 255),
        new GameColor(4, "Sky", 120, 165, 215), new GameColor(5, "Sea", 140, 225, 175),
        new GameColor(6, "Forest", 85, 155, 60), new GameColor(7, "Sod", 170, 190, 95),
        new GameColor(8, "Cream", 255, 254, 208), new GameColor(9, "Sun", 240, 225, 80),
        new GameColor(10, "Gold", 255, 195, 5), new GameColor(11, "Cadmium", 250, 105, 65),
        new GameColor(12, "Sanguine", 95, 10, 0), new GameColor(13, "Mocha", 75, 40, 0),
        new GameColor(14, "Matte", 30, 30, 30), new GameColor(15, "Cobalt", 0, 50, 120)
    );
  }

  public static ColorPalette original() {
    return new ColorPalette("Original",
        new GameColor(0, "Cadmium", 240, 130, 50), new GameColor(1, "Sun", 241, 224, 82),
        new GameColor(2, "Sod", 171, 191, 94), new GameColor(3, "Sea", 140, 198, 183),
        new GameColor(4, "Sky", 122, 165, 217), new GameColor(5, "Burgundy", 111, 16, 56),
        new GameColor(6, "Sanguine", 104, 7, 2), new GameColor(7, "Bistre", 84, 35, 25),
        new GameColor(8, "Forest", 32, 64, 18), new GameColor(9, "Cobalt", 32, 42, 122),
        new GameColor(10, "Indigo", 39, 25, 79), new GameColor(11, "Amethyst", 82, 28, 124),
        new GameColor(12, "Gold", 227, 170, 49), new GameColor(13, "Tenné", 193, 146, 114),
        new GameColor(14, "Gunmetal", 170, 159, 140), new GameColor(15, "Aphotic", 64, 49, 51)
    );
  }

//  public static ColorPalette grayscale() { // TODO: Doesn't really work, maybe 8-color palette?
//    return new ColorPalette("Grayscale",
//        new GameColor(0, "Ceramic", 255, 255, 255), new GameColor(1, "Paper", 231, 231, 231),
//        new GameColor(2, "Cloud", 209, 209, 209), new GameColor(3, "Fog", 189, 189, 189),
//        new GameColor(4, "Aluminum", 171, 171, 171), new GameColor(5, "Stardust", 155, 155, 155),
//        new GameColor(6, "Monsoon", 140, 140, 140), new GameColor(7, "Granite", 126, 126, 126),
//        new GameColor(8, "Smoke", 113, 113, 113), new GameColor(9, "Carbon", 100, 100, 100),
//        new GameColor(10, "Charcoal", 86, 86, 86), new GameColor(11, "Iridium", 71, 71, 71),
//        new GameColor(12, "Peppercorn", 56, 56, 56), new GameColor(13, "Graphite", 40, 40, 40),
//        new GameColor(14, "Obsidian", 24, 24, 24), new GameColor(15, "Onyx", 0, 0, 0)
//    );
//  }

//  public static ColorPalette pastel() { // TODO: Good, but needs work
//    return new ColorPalette("Pastel",
//        new GameColor(0, "Champagne", "#FFF7E4"), new GameColor(1, "Daffodil", "#FFF7A0"),
//        new GameColor(2, "Chardonnay", "#FFC384"), new GameColor(3, "Geraldine", "#DEA38B"),
//        new GameColor(4, "Tequila", "#FFE6C6"), new GameColor(5, "Sweetcorn", "#E9F59D"),
//        new GameColor(6, "Veltliner", "#B0EB93"), new GameColor(7, "Sage", "#87A889"),
//        new GameColor(8, "Orchid", "#FEAAE4"), new GameColor(9, "Edgewater", "#B3E3DA"),
//        new GameColor(10, "Cornflower", "#ACCCE4"), new GameColor(11, "Biloba", "#B0A9E4"),
//        new GameColor(12, "Rosé", "#F98284"), new GameColor(13, "Carnation", "#D9C8BF"),
//        new GameColor(14, "Allium", "#6C5671"), new GameColor(15, "Baccara", "#28282E")
//    );
//  }

//  public static ColorPalette antiquity() { // TODO: Good, but needs work
//    return new ColorPalette("Antiquity",
//        new GameColor(0, "Hampton", "#E8D8A5"), new GameColor(1, "Ochre", "#DE9A28"),
//        new GameColor(2, "Sienna", "#D26730"), new GameColor(3, "Coral", "#F1866C"),
//        new GameColor(4, "Valentine", "#E55D4D"), new GameColor(5, "Cascade", "#8AA7AC"),
//        new GameColor(6, "Raven", "#707B88"), new GameColor(7, "Olive", "#8E9257"),
//        new GameColor(8, "Fern", "#5D7557"), new GameColor(9, "Apache", "#E8BE82"),
//        new GameColor(10, "Sepia", "#E89F6E"), new GameColor(11, "Clay", "#B16B4A"),
//        new GameColor(12, "Tawny", "#6D3D29"), new GameColor(13, "Bistre", "#452923"),
//        new GameColor(14, "Umber", "#2D211E"), new GameColor(15, "Sable", "#202020")
//    );
//  }

  private static ColorPalette PLACEHOLDER() { // Just a placeholder for copy-pasting
    return new ColorPalette("PLACEHOLDER",
        new GameColor(0, "NAME", ""), new GameColor(1, "NAME", ""),
        new GameColor(2, "NAME", ""), new GameColor(3, "NAME", ""),
        new GameColor(4, "NAME", ""), new GameColor(5, "NAME", ""),
        new GameColor(6, "NAME", ""), new GameColor(7, "NAME", ""),
        new GameColor(8, "NAME", ""), new GameColor(9, "NAME", ""),
        new GameColor(10, "NAME", ""), new GameColor(11, "NAME", ""),
        new GameColor(12, "NAME", ""), new GameColor(13, "NAME", ""),
        new GameColor(14, "NAME", ""), new GameColor(15, "NAME", "")
    );
  }

}
