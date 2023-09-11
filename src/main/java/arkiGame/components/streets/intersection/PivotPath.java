package arkiGame.components.streets.intersection;

import arkiGame.components.vehicles.Sedan;
import arkiGame.components.vehicles.Vehicle;
import l0raxeo.arki.engine.components.Component;
import l0raxeo.arki.engine.gameObjects.GameObject;
import l0raxeo.arki.engine.scenes.SceneManager;
import org.joml.Vector2i;

import java.awt.*;

import static arkiGame.scenes.IntersectionSimulation.BLOCK_HEIGHT;
import static arkiGame.scenes.IntersectionSimulation.BLOCK_WIDTH;

public class PivotPath implements Path {

    private final Vehicle vehicle;

    private final Vector2i pivotPoint;
    private final Vector2i endPoint;

    private boolean passedPivotPoint = false;

    public PivotPath(Vehicle vehicle, Vector2i pivotPoint, StreetPath destination) {
        this.vehicle = vehicle;
        this.pivotPoint = pivotPoint;
        this.endPoint = destination.getBlockPosition(0);
    }

    @Override
    public Vector2i getMoveCoordinateInstructions(GameObject gameObject) {
        if (gameObject.transform.getScreenPosition().x == endPoint.x && gameObject.transform.getScreenPosition().y == endPoint.y) {
            for (Component c : gameObject.getAllComponents()) {
                if (Vehicle.class.isAssignableFrom(c.getClass())) {
                    ((Vehicle) c).getPath2().addVehicleToPath(gameObject);
                    ((Vehicle) c).setCurrentPath(((Vehicle) c).getPath2());
                }
            }
        }

        if (gameObject.transform.getScreenPosition().x == pivotPoint.x && gameObject.transform.getScreenPosition().y == pivotPoint.y)
            passedPivotPoint = true;

        if (!passedPivotPoint) {
            return pivotPoint;
        } else {
            return endPoint;
        }
    }

    public Vector2i getNextBlockPosition(GameObject gameObject, Vehicle vehicle) {

        Vector2i nextBlock = getMoveCoordinateInstructions(gameObject);
        Vector2i rawDirection = new Vector2i((int) (nextBlock.x - gameObject.transform.getScreenPosition().x), (int) (nextBlock.y - gameObject.transform.getScreenPosition().y));
        Vector2i velocity = new Vector2i((rawDirection.x != 0 ? rawDirection.x / Math.abs(rawDirection.x) : 0), (rawDirection.y != 0 ? rawDirection.y / Math.abs(rawDirection.y) : 0)); // direction in terms of 1 and 0

        Vector2i currentPosition = new Vector2i((int) gameObject.transform.getScreenPosition().x, (int) gameObject.transform.getScreenPosition().y);
        Vector2i nextBlockPosition = new Vector2i();
        nextBlockPosition.x = (((currentPosition.x + ((BLOCK_WIDTH / 2) * velocity.x)) / BLOCK_WIDTH) * BLOCK_WIDTH) + (BLOCK_WIDTH / 2);
        nextBlockPosition.y = (((currentPosition.y + ((BLOCK_HEIGHT / 2) * velocity.y)) / BLOCK_HEIGHT) * BLOCK_HEIGHT) + (BLOCK_HEIGHT / 2);

        return nextBlockPosition;
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
