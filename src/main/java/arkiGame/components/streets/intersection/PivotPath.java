package arkiGame.components.streets.intersection;

import arkiGame.components.vehicles.Vehicle;
import l0raxeo.arki.engine.gameObjects.GameObject;
import org.joml.Vector2i;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static arkiGame.scenes.IntersectionSimulation.BLOCK_HEIGHT;
import static arkiGame.scenes.IntersectionSimulation.BLOCK_WIDTH;

public class PivotPath implements Path {

    public static List<GameObject> vehicleGosOnPivotPaths = new ArrayList<>();

    private final Vehicle vehicle;

    private final Vector2i pivotPoint;
    private final Vector2i endPoint;

    private Vector2i nextBlockPosition = new Vector2i();

    private boolean passedPivotPoint = false;

    public PivotPath(Vehicle vehicle, Vector2i pivotPoint, StreetPath destination) {
        this.vehicle = vehicle;
        this.pivotPoint = pivotPoint;
        this.endPoint = destination.getBlockPosition(0);
    }

    @Override
    public Vector2i getMoveCoordinateInstructions(GameObject gameObject) {
        Vector2i moveCoordResult;

        if (gameObject.transform.getScreenPosition().x == endPoint.x && gameObject.transform.getScreenPosition().y == endPoint.y) {
            vehicle.getPath2().addVehicleToPath(gameObject);
            vehicle.setCurrentPath(vehicle.getPath2());
        }

        if (gameObject.transform.getScreenPosition().x == pivotPoint.x && gameObject.transform.getScreenPosition().y == pivotPoint.y)
            passedPivotPoint = true;

        if (!passedPivotPoint) {
            moveCoordResult = pivotPoint;
        } else {
            moveCoordResult = endPoint;
        }

        updateNextBlockPosition(gameObject, moveCoordResult);

        // check for collisions
        for (GameObject gameObject1 : PivotPath.vehicleGosOnPivotPaths) {
            if (gameObject1.getUid() == gameObject.getUid() || gameObject1.getName().equals("intersection"))
                continue;

            Vehicle vehicle1 = (Vehicle) gameObject1.getInterfaceComponent(Vehicle.class);

            boolean collisionDetected = gameObject1.transform.boundsContain(getNextBlockPosition());
            if (collisionDetected) {
                moveCoordResult = new Vector2i((int) gameObject.transform.getScreenPosition().x, (int) gameObject.transform.getScreenPosition().y);
            }

            boolean possibleCollisionDetected = (vehicle1.getPivotPath().getNextBlockPosition().equals(getNextBlockPosition().x, getNextBlockPosition().y));
            if (possibleCollisionDetected) {
                int distanceFromNextBlock = (int) (Math.abs(getNextBlockPosition().x - gameObject.transform.getScreenPosition().x) + Math.abs(getNextBlockPosition().y - gameObject.transform.getScreenPosition().y));
                int distanceFromNextBlock1 = (int) (Math.abs(vehicle1.getPivotPath().getNextBlockPosition().x - gameObject1.transform.getScreenPosition().x) + Math.abs(vehicle1.getPivotPath().getNextBlockPosition().y - gameObject1.transform.getScreenPosition().y));

                if (distanceFromNextBlock > distanceFromNextBlock1) {
                    moveCoordResult = new Vector2i((int) gameObject.transform.getScreenPosition().x, (int) gameObject.transform.getScreenPosition().y);
                }
            }
        }

        return moveCoordResult;
    }

    public void updateNextBlockPosition(GameObject gameObject, Vector2i moveCoordinateInstructions) {
        Vector2i rawDirection = new Vector2i((int) (moveCoordinateInstructions.x - gameObject.transform.getScreenPosition().x), (int) (moveCoordinateInstructions.y - gameObject.transform.getScreenPosition().y));
        Vector2i velocity = new Vector2i((rawDirection.x != 0 ? rawDirection.x / Math.abs(rawDirection.x) : 0), (rawDirection.y != 0 ? rawDirection.y / Math.abs(rawDirection.y) : 0)); // direction in terms of 1 and 0

        Vector2i currentPosition = new Vector2i((int) gameObject.transform.getScreenPosition().x, (int) gameObject.transform.getScreenPosition().y);
        Vector2i nextBlockPosition = new Vector2i();
        nextBlockPosition.x = (((currentPosition.x + ((BLOCK_WIDTH / 2) * velocity.x)) / BLOCK_WIDTH) * BLOCK_WIDTH) + (BLOCK_WIDTH / 2);
        nextBlockPosition.y = (((currentPosition.y + ((BLOCK_HEIGHT / 2) * velocity.y)) / BLOCK_HEIGHT) * BLOCK_HEIGHT) + (BLOCK_HEIGHT / 2);

        this.nextBlockPosition = nextBlockPosition;
    }

    public Vector2i getNextBlockPosition() {
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
