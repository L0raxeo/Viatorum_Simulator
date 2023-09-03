package arkiGame.scenes;

import arkiGame.prefabs.StreetPrefabs;
import l0raxeo.arki.engine.scenes.DefaultScene;
import l0raxeo.arki.engine.scenes.Scene;

import java.awt.*;

@DefaultScene()
public class IntersectionSimulation extends Scene {

    public static final int BLOCK_WIDTH = 60;
    public static final int BLOCK_HEIGHT = 60;

    @Override
    public void init() {
        setBackdrop(Color.WHITE);
    }

    @Override
    public void start() {
        addGameObject(StreetPrefabs.fabricateIntersection());
    }

    @Override
    public void update(double dt) {
        updateSceneGameObjects(dt);
    }

    @Override
    public void render(Graphics g) {
        renderSceneGameObjects(g);
    }

}
