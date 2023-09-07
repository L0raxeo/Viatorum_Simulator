package arkiGame.components.vehicles;

import arkiGame.components.menu.SimulationMenu;
import arkiGame.components.streets.intersection.StreetPath;
import arkiGame.prefabs.VehiclePrefabs;
import arkiGame.scenes.IntersectionSimulation;
import l0raxeo.arki.engine.components.Component;
import l0raxeo.arki.engine.gameObjects.GameObject;
import l0raxeo.arki.engine.scenes.SceneManager;

public class VehicleManager extends Component {

    private final StreetPath[] enterPaths = new StreetPath[4];
    private final StreetPath[] exitPaths = new StreetPath[4];
    private boolean isRunning = false;

    private IntersectionSimulation scene;

    private void spawnVehicles() {
        // THIS CODE SPAWNS VEHICLES FOR MULTIPLE PATHS

        for (StreetPath path1 : enterPaths) {
            boolean canSpawnVehicle = /*path1.getVehicleOnBlock(-1) == null &&*/ path1.getVehicleOnBlock(0) == null;
            if (!canSpawnVehicle)
                continue;

            boolean foundExitPath = false;
            int randomExitPathIndex = -1;

            while (!foundExitPath) {
                randomExitPathIndex = (int) (Math.random() * (4));
                foundExitPath = exitPaths[randomExitPathIndex].getAvenue() != path1.getAvenue();
            }

            GameObject vehicle = VehiclePrefabs.fabricateSedan(path1, exitPaths[randomExitPathIndex]);
            path1.addVehicleToPath(vehicle);
            scene.vehiclesToAdd.add(vehicle);
        }

        // THIS CODE SPAWNS VEHICLES FOR ONE PATH ONLY
//        StreetPath path1 = enterPaths[1];
//        StreetPath path2 = exitPaths[2];
//        boolean canSpawnVehicle = /*path1.getVehicleOnBlock(-1) == null &&*/path1.getVehicleOnBlock(0) == null;
//        if (canSpawnVehicle) {
//            GameObject curVehicle = VehiclePrefabs.fabricateSedan(path1, path2);
//            path1.addVehicleToPath(curVehicle);
//            scene.vehiclesToAdd.add(curVehicle);
//        }
    }

    private void subscribeToEvents() {
        SimulationMenu.startSimulationEvent.subscribe(this, "onSimulationStart");
        SimulationMenu.stopSimulationEvent.subscribe(this, "onSimulationStop");
    }

    private void initializePaths() {
        for (StreetPath path : gameObject.getComponents(StreetPath.class)) {
            boolean isEnterPath = path.getId().contains("enter");
            if (isEnterPath) {
                enterPaths[Integer.parseInt(path.getId().substring(0, 1))] = path;
            }
            else {
                exitPaths[Integer.parseInt(path.getId().substring(0, 1))] = path;
            }
        }
    }

    @Override
    public void start() {
        subscribeToEvents();
        initializePaths();

        scene = (IntersectionSimulation) SceneManager.getActiveScene();
    }

    @Override
    public void update(double dt) {
        if (!isRunning)
            return;

        spawnVehicles();
    }

    public void onSimulationStart() {
        isRunning = true;

        // test code
//        Path path1 = enterPaths[0];
//        Path path2 = exitPaths[1];
//        boolean canSpawnVehicle = path1.getVehicleOnBlock(-1) == null && path1.getVehicleOnBlock(0) == null;
//        if (canSpawnVehicle) {
//            GameObject curVehicle = VehiclePrefabs.fabricateSedan(path1, path2);
//            path1.addVehicleToPath(curVehicle);
//            scene.vehiclesToAdd.add(curVehicle);
//        }
    }

    public void onSimulationStop() {
        isRunning = false;
    }

}
