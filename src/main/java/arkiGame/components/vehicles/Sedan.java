package arkiGame.components.vehicles;

import arkiGame.components.menu.SimulationMenu;
import arkiGame.components.streets.intersection.Path;
import arkiGame.components.streets.intersection.PivotPath;
import arkiGame.components.streets.intersection.StreetPath;
import l0raxeo.arki.engine.assetFiles.AssetPool;
import l0raxeo.arki.engine.components.Component;
import l0raxeo.arki.engine.ui.GuiText;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static arkiGame.components.vehicles.VehicleType.SEDAN;
import static arkiGame.scenes.IntersectionSimulation.debugging;

public class Sedan extends Component implements Vehicle {

    private final int uid;
    private final int passangerCount;
    private final EngineType engineType;

    private final StreetPath path1;
    private final PivotPath pivotPath;
    private final StreetPath path2;

    private Path currentPath;

    public Sedan(int uid, int passangerCount, EngineType engineType, StreetPath path1, StreetPath path2) {
        this.uid = uid;
        this.passangerCount = passangerCount;
        this.engineType = engineType;

        this.path1 = path1;
        this.path2 = path2;
        this.currentPath = path1;
        this.pivotPath = new PivotPath(this, findPivotPoint(), path2);
        SimulationMenu.stopSimulationEvent.subscribe(this, "onSimulationStop");
    }

    private Vector2i findPivotPoint() {
        int path1Id = path1.getAvenue();
        int path2Id = path2.getAvenue();

        int ANGLED_PATH = 1;
        int STRAIGHT_PATH = 2;
        int HORIZONTAL = 0;
        int VERTICAL = 1;

        int path1Orientation = (path1Id + 1) % 2 == 0 ? HORIZONTAL : VERTICAL;
        int path2Orientation = (path2Id + 1) % 2 == 0 ? HORIZONTAL : VERTICAL;

        int PATH_BEHAVIOR = path1Orientation != path2Orientation ? ANGLED_PATH : STRAIGHT_PATH;

        if (PATH_BEHAVIOR == ANGLED_PATH) {
            int verticalX = path1Orientation == VERTICAL ? path1.getBlockPosition(1).x : path2.getBlockPosition(1).x;
            int horizontalY = path1Orientation == HORIZONTAL ? path1.getBlockPosition(1).y : path2.getBlockPosition(1).y;
            return new Vector2i(verticalX, horizontalY);
        }
        else /*if (PATH_BEHAVIOR == STRAIGHT_PATH)*/ {
            return new Vector2i(path2.getBlockPosition(-1).x, path2.getBlockPosition(-1).y);
        }
    }

    private void move() {
        Vector2i nextBlock = currentPath.getMoveCoordinateInstructions(gameObject);
        Vector2i rawDirection = new Vector2i((int) (nextBlock.x - gameObject.transform.getScreenPosition().x), (int) (nextBlock.y - gameObject.transform.getScreenPosition().y));
        Vector2i direction = new Vector2i((rawDirection.x != 0 ? rawDirection.x / Math.abs(rawDirection.x) : 0), (rawDirection.y != 0 ? rawDirection.y / Math.abs(rawDirection.y) : 0)); // direction in terms of 1 and 0
        Vector2f previousPosition = gameObject.transform.getScreenPosition();
        gameObject.transform.setScreenPosition(previousPosition.x + direction.x, previousPosition.y + direction.y);
    }

    @Override
    public void update(double dt) {
        move();
    }

    @Override
    public void render(Graphics g) {
        Vector2i screenPosition = new Vector2i((int) gameObject.transform.getScreenPosition().x, (int) gameObject.transform.getScreenPosition().y);
        g.setColor(Color.DARK_GRAY);

        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();
        g2d.rotate(Math.toRadians(gameObject.transform.rotation), screenPosition.x, screenPosition.y);
        g2d.fillRect((int) (screenPosition.x - (gameObject.transform.scale.x / 2)), (int) (screenPosition.y - (gameObject.transform.scale.y / 2)), (int) gameObject.transform.scale.x, (int) gameObject.transform.scale.y);
        g2d.setTransform(old);

        if (debugging) {
            pivotPath.debugRender(g);
        }

        if (currentPath.equals(pivotPath)) {
            g.setColor(Color.RED);
            g.fillRect(pivotPath.getNextBlockPosition().x - 5, pivotPath.getNextBlockPosition().y - 5, 10, 10);
            g.drawLine(pivotPath.getNextBlockPosition().x, pivotPath.getNextBlockPosition().y, (int) gameObject.transform.getScreenPosition().x, (int) gameObject.transform.getScreenPosition().y);
        }

        GuiText.drawString(
                g,
                String.valueOf(gameObject.getUid()),
                (int) gameObject.transform.getScreenPosition().x, (int) gameObject.transform.getScreenPosition().y,
                true,
                Color.WHITE,
                AssetPool.getFont("default_font")
        );
    }

    public void onSimulationStop() {
        gameObject.die();
    }

    public Vector2i getPivotPoint() {
        return this.pivotPath.getPivotPoint();
    }

    public PivotPath getPivotPath() {
        return this.pivotPath;
    }

    public StreetPath getPath1() {
        return this.path1;
    }

    public StreetPath getPath2() {
        return this.path2;
    }

    public void setCurrentPath(Path path) {
        if (path.equals(getPivotPath())) {
            PivotPath.vehicleGosOnPivotPaths.add(gameObject);
        }
        else if (path.equals(getPath2())) {
            PivotPath.vehicleGosOnPivotPaths.remove(gameObject);
        }

        this.currentPath = path;
    }

    @Override
    public Path getCurrentPath() {
        return currentPath;
    }

    @Override
    public String toString() {
        return uid + "_" + passangerCount + "_" + SEDAN + "_" + engineType;
    }

}
