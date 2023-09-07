package arkiGame.components.vehicles;

import arkiGame.components.streets.intersection.Path;
import arkiGame.components.streets.intersection.PivotPath;
import arkiGame.components.streets.intersection.StreetPath;
import org.joml.Vector2i;

public interface Vehicle {

    String toString();

    Vector2i getPivotPoint();

    PivotPath getPivotPath();

    StreetPath getPath1();

    StreetPath getPath2();

    void setCurrentPath(Path path);

}
