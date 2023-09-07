package arkiGame.prefabs;

import arkiGame.components.streets.intersection.StreetPath;
import arkiGame.components.vehicles.EngineType;
import arkiGame.components.vehicles.Sedan;
import l0raxeo.arki.engine.gameObjects.GameObject;
import l0raxeo.arki.engine.gameObjects.prefabs.Prefabs;
import l0raxeo.arki.renderer.AppWindow;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import static arkiGame.components.vehicles.VehicleType.SEDAN;

public class VehiclePrefabs {

    public static int VEHICLE_COUNT = 0;

    public static GameObject fabricateSedan(StreetPath path1, StreetPath path2) {
        // TODO CHANGE PROBABILITIES BASED ON USER INPUT
        int passengerCount = (int) ((Math.random() * SEDAN.getMaxCapacity()) + 1);
        EngineType engineType = (int) (Math.random() * 2) == 0 ? EngineType.GAS : EngineType.ELECTRIC;
        Vector2i position = path1.getBlockPosition(-1);
        int avenue = Integer.parseInt(path1.getId().substring(0, 1));
        float rotation = (avenue + 1) % 2 == 0 ? 90 : 0;

        return Prefabs.generate(
                String.valueOf(VEHICLE_COUNT++),
                new Vector3f(position.x, AppWindow.WINDOW_HEIGHT - position.y, 1),
                new Vector2f(30, 50),
                    rotation,
                    new Sedan(
                            VEHICLE_COUNT,
                            passengerCount,
                            engineType,
                            path1,
                            path2
                    )
        );
    }

    public static void fabricateSportUtilityVehicle() {

    }

    public static void fabricateBuses() {

    }

}
