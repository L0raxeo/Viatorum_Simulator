package arkiGame.prefabs;

import arkiGame.components.streets.intersection.IntersectionDrawer;
import l0raxeo.arki.engine.gameObjects.prefabs.Prefabs;
import l0raxeo.arki.renderer.AppWindow;
import org.joml.Vector3f;

public class StreetPrefabs {

    public static void fabricateIntersection() {
        Prefabs.generate(
                "intersection",
                new Vector3f(),
                AppWindow.getInstance().getWindowSize(),
                new IntersectionDrawer()
        );
    }

    public static void fabricateRoundabout() {

    }

}
