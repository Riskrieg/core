import static org.junit.jupiter.api.Assertions.assertTrue;

import com.riskrieg.map.RkmMap;
import java.net.URL;
import org.junit.jupiter.api.Test;

public class MapTests {

  @Test
  public void loadMapFromUrl() {
    try {
      var optMap = RkmMap.load(new URL("https://github.com/Riskrieg/core/raw/v2/res/maps/antarctica.rkm"));
      assertTrue(optMap.isPresent());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
