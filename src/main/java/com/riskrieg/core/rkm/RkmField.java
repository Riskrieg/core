package com.riskrieg.core.rkm;

import java.nio.charset.StandardCharsets;

public enum RkmField {

  /**
   * The code name of the map.
   * Field name: MCNM
   * Usage: MCNM[length-in-bytes][string-as-bytes]
   */
  MAP_CODE_NAME("MCNM"),

  /**
   * The display name of the map.
   * Field name: MDNM
   * Usage: MDNM[length-in-bytes][string-as-bytes]
   */
  MAP_DISPLAY_NAME("MDNM"),

  /**
   * The name of the author of the map.
   * Field name: MATN
   * Usage: MATN[length-in-bytes][string-as-bytes]
   */
  MAP_AUTHOR_NAME("MATN"),

  /**
   * The list of (x, y) coordinates that define the vertices of the map graph. Each (x, y) pair is considered a 'nucleus', and each vertex can have multiple nuclei.
   * Field name: VTCS
   * Usage: VTCS[length-in-bytes][number-of-vertices-as-int][vertex1][vertex2]...[vertexN]
   * Vertex Format: [string-id-length-in-bytes][string-id][number-of-nuclei][x1][y1][x2][y2]...[xN][yN]
   */
  VERTICES("VTCS"),

  /**
   * The list of vertex ID pairs that define all the connections between vertices. Each edge defines a single connection between two different vertices.
   * Field name: EDGS
   * Usage: EDGS[length-in-bytes][number-of-edges-as-int][edge1][edge2]...[edgeN]
   * Edge Format: [source-string-id-length-in-bytes][source-string-id][target-string-id-length-in-bytes][target-string-id]
   */
  EDGES("EDGS"),

  /**
   * The PNG data that defines the base layer of the map image.
   * Field name: MIBS
   * Usage: MIBS[length-in-bytes][image-encoded-as-png-bytes]
   */
  MAP_IMAGE_BASE("MIBS"),

  /**
   * The PNG data that defines the text layer of the map image.
   * Field name: MITX
   * Usage: MITX[length-in-bytes][image-encoded-as-png-bytes]
   */
  MAP_IMAGE_TEXT("MITX"),

  /**
   * The file checksum that covers everything in the file from the very beginning all the way until the point the checksum is written.
   * This is also considered an end-of-file marker.
   * Field name: CKSM
   * Usage: CKSM[length-in-bytes][checksum-data-as-bytes]
   */
  CHECKSUM("CKSM");

  private final byte[] fieldName;

  RkmField(String fieldName) {
    if (fieldName.getBytes(StandardCharsets.UTF_8).length != 4) {
      throw new IllegalArgumentException("String 'fieldName' length must be exactly four (4) bytes");
    }
    this.fieldName = fieldName.getBytes(StandardCharsets.UTF_8);
  }

  public byte[] fieldName() {
    return fieldName;
  }

}
