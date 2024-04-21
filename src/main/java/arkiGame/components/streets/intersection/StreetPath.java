package arkiGame.components.streets.intersection;

import arkiGame.components.menu.SimulationMenu;
import arkiGame.components.stoplights.Stoplight;
import arkiGame.components.stoplights.StoplightSystem;
import arkiGame.components.vehicles.Vehicle;
import l0raxeo.arki.engine.assetFiles.AssetPool;
import l0raxeo.arki.engine.components.Component;
import l0raxeo.arki.engine.gameObjects.GameObject;
import l0raxeo.arki.engine.scenes.SceneManager;
import l0raxeo.arki.engine.ui.GuiText;
import org.joml.Vector2i;

import java.awt.*;
import java.util.Arrays;

import static arkiGame.scenes.IntersectionSimulation.*;

public class StreetPath extends Component implements Path {

    // Constants
    public final static int MAX_NUMBER_OF_BLOCKS = 4;

    // Vehicle Managing
    private GameObject[] vehiclesOnPath = new GameObject[6];

    // Path info
    private final String id;
    private int xPathDirectionMultiplier, yPathDirectionMultiplier;
    private final Vector2i startingBlockPosition;

    public StreetPath(String id, Vector2i startingBlockPosition, Vector2i endingBlockPosition) {
        this.id = id;
        this.startingBlockPosition = startingBlockPosition;
        this.xPathDirectionMultiplier = (endingBlockPosition.x - startingBlockPosition.x);

        if (endingBlockPosition.x - startingBlockPosition.x != 0) {
            this.xPathDirectionMultiplier /= Math.abs(endingBlockPosition.x - startingBlockPosition.x);
        }

        this.yPathDirectionMultiplier = (endingBlockPosition.y - startingBlockPosition.y);

        if (endingBlockPosition.y - startingBlockPosition.y != 0) {
            this.yPathDirectionMultiplier /= Math.abs(endingBlockPosition.y - startingBlockPosition.y);
        }

        SimulationMenu.stopSimulationEvent.subscribe(this, "onSimulationStop");
    }

    private int getNextBlock(GameObject vehicle) {
        for (int i = 0; i < vehiclesOnPath.length; i++) {
            if (vehiclesOnPath[i] != null && vehiclesOnPath[i].getUid() == vehicle.getUid()) {
                return i + 1;
            }
        }

        return -1;
    }

    @Override
    public Vector2i getMoveCoordinateInstructions(GameObject vehicle) {
        // get next block and next block position
        int nextBlock = getNextBlock(vehicle);
        Vector2i nextBlockPosition = getBlockPosition(nextBlock);

        // destroy any vehicles that exit path 2
        if (getId().contains("exit") && nextBlock >= 5) {
            removeVehicle(vehicle);
            vehicle.die();
            return new Vector2i((int) vehicle.transform.getScreenPosition().x, (int) vehicle.transform.getScreenPosition().y);
        }

        // update array with most recent position
        assert nextBlockPosition != null;
        if (vehicle.transform.getScreenPosition().x == nextBlockPosition.x && vehicle.transform.getScreenPosition().y == nextBlockPosition.y) {
            vehiclesOnPath[nextBlock] = vehicle;
            vehiclesOnPath[nextBlock - 1] = null;
            // refresh the variables with new changes
            nextBlock = getNextBlock(vehicle);
            nextBlockPosition = getBlockPosition(nextBlock);
        }

        if (getId().contains("enter")) {
            return getEnterPathMoveCoordinateInstructions(vehicle, nextBlock, nextBlockPosition);
        } else {
            return getExitPathMoveCoordinateInstructions(vehicle, nextBlock, nextBlockPosition);
        }
    }

    private Vector2i getEnterPathMoveCoordinateInstructions(GameObject vehicle, int nextBlock, Vector2i nextBlockPosition) {
        // fail safe if behind a stoplight to stop collisions
        boolean isBehindStoplight = nextBlock == 5;
        if (isBehindStoplight) {
            for (GameObject possibleCollision : SceneManager.getActiveScene().getGameObjects()) {
                if (possibleCollision.getUid() == vehicle.getUid() || possibleCollision.hasComponent(StoplightSystem.class))
                    continue;

                if (possibleCollision.transform.boundsContain(nextBlockPosition)) {
                    return new Vector2i((int) vehicle.transform.getScreenPosition().x, (int) vehicle.transform.getScreenPosition().y);
                }
            }
        }

        if (nextBlock < 5) { // is behind the stoplight
            // check if space ahead is empty
            boolean isNextBlockEmpty = getVehicleOnBlock(nextBlock) == null;
            if (isNextBlockEmpty) {
                return nextBlockPosition;
            }
        } else if (nextBlock == 5) { // is next to the stoplight
            StoplightSystem stoplightSystem = gameObject.getComponent(StoplightSystem.class);
            switch (stoplightSystem.getStoplight(this)) {
                case GREEN -> {
                    return nextBlockPosition;
                }
                case YELLOW -> {
                    return nextBlockPosition;
                }
                case RED -> {

                }
            }
        }
        else {// is on the stoplight
            // get the vehicle component
            Vehicle vehicleComponent = null;
            for (Component c : vehicle.getAllComponents()) {
                if (Vehicle.class.isAssignableFrom(c.getClass())) {
                    vehicleComponent = (Vehicle) c;
                }
            }

            // set the current path to the pivot path
            assert vehicleComponent != null;
            vehicleComponent.setCurrentPath(vehicleComponent.getPivotPath());
            removeVehicle(vehicle);
        }

        return new Vector2i((int) vehicle.transform.getScreenPosition().x, (int) vehicle.transform.getScreenPosition().y);
    }

    private Vector2i getExitPathMoveCoordinateInstructions(GameObject vehicle, int nextBlock, Vector2i nextBlockPosition) {
         if (nextBlock == -1) {
             removeVehicle(vehicle);
         }
        return getBlockPosition(5);
//        return new Vector2i((int) vehicle.transform.getScreenPosition().x, (int) vehicle.transform.getScreenPosition().y);
    }

    @Override
    public Vector2i getBlockPosition(int block) {
        int x = startingBlockPosition.x + ((block * BLOCK_WIDTH * xPathDirectionMultiplier) - (BLOCK_WIDTH * xPathDirectionMultiplier));
        int y = startingBlockPosition.y + ((block * BLOCK_HEIGHT * yPathDirectionMultiplier) - (BLOCK_HEIGHT * yPathDirectionMultiplier));
        return new Vector2i(x, y);
    }

    public void removeVehicle(GameObject vehicle) {
        for (int i = 0; i < vehiclesOnPath.length; i++) {
            if (vehiclesOnPath[i] != null && vehicle.getUid() == vehiclesOnPath[i].getUid()) {
                vehiclesOnPath[i] = null;
            }
        }
    }

    public void addVehicleToPath(GameObject vehicle) {
        vehiclesOnPath[0] = vehicle;
    }

    public GameObject getVehicleOnBlock(int block) {
        return vehiclesOnPath[block];
    }

    @Override
    public void render(Graphics g) {
        if (!debugging)
            return;

        for (int i = 0; i < 6; i++) {
            GuiText.drawString(
                    g,
                    String.valueOf(i),
                    getBlockPosition(i),
                    true,
                    Color.RED,
                    AssetPool.getFont("default_font")
            );
        }
    }

    public void onSimulationStop() {
        Arrays.fill(vehiclesOnPath, null);
    }

    public String getId() {
        return this.id;
    }

    public int getAvenue() {
        return Integer.parseInt(getId().substring(0, 1));
    }

}
