package arkiGame.prefabs;

import arkiGame.components.streets.intersection.IntersectionDrawer;
import arkiGame.components.streets.intersection.StreetPath;
import arkiGame.components.stoplights.StoplightSystem;
import arkiGame.components.vehicles.VehicleManager;
import l0raxeo.arki.engine.gameObjects.GameObject;
import l0raxeo.arki.engine.gameObjects.prefabs.Prefabs;
import l0raxeo.arki.renderer.AppWindow;
import org.joml.Vector2i;
import org.joml.Vector3f;

public class StreetPrefabs {

    public static GameObject fabricateIntersection() {
        return Prefabs.generate(
                "intersection",
                new Vector3f(0),
                AppWindow.getInstance().getWindowSize(),
                new IntersectionDrawer(),
                new StreetPath("3_enter", new Vector2i(30, 390), new Vector2i(210, 390)), // avenue 4 --> right
                new StreetPath("1_exit", new Vector2i(510, 390), new Vector2i(690, 390)), // avenue 2 --> right
                new StreetPath("3_exit", new Vector2i(210, 330), new Vector2i(30, 330)), // avenue 4 --> left
                new StreetPath("1_enter", new Vector2i(690, 330), new Vector2i(510, 330)), // avenue 2 --> left
                new StreetPath("0_enter", new Vector2i(330, 30), new Vector2i(330, 210)), // avenue 1 --> down
                new StreetPath("0_exit", new Vector2i(390, 210), new Vector2i(390, 30)), // avenue 1 --> up
                new StreetPath("2_exit", new Vector2i(330, 510), new Vector2i(330, 690)), // avenue 3 --> down
                new StreetPath("2_enter", new Vector2i(390, 690), new Vector2i(390, 510)), // avenue 3 --> up
                new StoplightSystem(),
                new VehicleManager()
        );
    }

    public static void fabricateRoundabout() {

    }

}
