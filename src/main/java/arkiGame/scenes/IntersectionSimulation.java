package arkiGame.scenes;

import l0raxeo.arki.engine.scenes.DefaultScene;
import l0raxeo.arki.engine.scenes.Scene;

import java.awt.*;

@DefaultScene()
public class IntersectionSimulation extends Scene {

    @Override
    public void start() {
        setBackdrop(Color.WHITE);
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
