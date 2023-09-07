package arkiGame.components.streets.intersection;

import l0raxeo.arki.engine.gameObjects.GameObject;
import org.joml.Vector2i;

public interface Path {

    Vector2i getBlockPosition(int block);
    Vector2i getMoveCoordinateInstructions(GameObject vehicle);

}
