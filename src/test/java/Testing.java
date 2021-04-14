import com.riskrieg.map.GameMap;
import java.net.URL;
import org.junit.jupiter.api.Test;

public class Testing {

  @Test
  public void testing() {
    try {
      GameMap.load(new URL("https://github.com/Riskrieg/core/raw/v2/res/maps/antarctica.rkm")).ifPresent(map -> System.out.println(map.mapName().displayName()));
    }catch(Exception e) {
      e.printStackTrace();
    }
  }

}
