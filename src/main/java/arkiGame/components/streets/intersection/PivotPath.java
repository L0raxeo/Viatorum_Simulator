package arkiGame.components.streets.intersection;

import arkiGame.components.vehicles.Vehicle;
import l0raxeo.arki.engine.components.Component;
import l0raxeo.arki.engine.gameObjects.GameObject;
import org.joml.Vector2i;

import java.awt.*;

public class PivotPath implements Path {

    private final Vector2i pivotPoint;
    private final Vector2i endPoint;

    private boolean passedPivotPoint = false;

    public PivotPath(Vector2i pivotPoint, StreetPath destination) {
        this.pivotPoint = pivotPoint;
        this.endPoint = destination.getBlockPosition(0);
    }

    @Override
    public Vector2i getMoveCoordinateInstructions(GameObject vehicle) {
        if (vehicle.transform.getScreenPosition().x == endPoint.x && vehicle.transform.getScreenPosition().y == endPoint.y) {
            for (Component c : vehicle.getAllComponents()) {
                if (Vehicle.class.isAssignableFrom(c.getClass())) {
                    ((Vehicle) c).getPath2().addVehicleToPath(vehicle);
                    ((Vehicle) c).setCurrentPath(((Vehicle) c).getPath2());
                }
            }
        }

        if (vehicle.transform.getScreenPosition().x == pivotPoint.x && vehicle.transform.getScreenPosition().y == pivotPoint.y)
            passedPivotPoint = true;

        if (!passedPivotPoint) {
            return pivotPoint;
        } else {
            return endPoint;
        }
    }

    @Override
    public Vector2i getBlockPosition(int block) {
        return null;
    }

    public void debugRender(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(pivotPoint.x - 5, pivotPoint.y - 5, 10, 10);
    }

    public Vector2i getPivotPoint() {
        return this.pivotPoint;
    }

}
