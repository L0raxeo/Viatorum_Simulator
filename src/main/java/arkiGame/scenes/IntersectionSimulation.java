package arkiGame.scenes;

import arkiGame.components.menu.SimulationMenu;
import arkiGame.prefabs.StreetPrefabs;
import arkiGame.prefabs.VehiclePrefabs;
import l0raxeo.arki.engine.assetFiles.AssetPool;
import l0raxeo.arki.engine.gameObjects.GameObject;
import l0raxeo.arki.engine.gameObjects.Transform;
import l0raxeo.arki.engine.input.keyboard.KeyManager;
import l0raxeo.arki.engine.scenes.DefaultScene;
import l0raxeo.arki.engine.scenes.Scene;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import static l0raxeo.arki.renderer.AppWindow.WINDOW_HEIGHT;
import static l0raxeo.arki.renderer.AppWindow.WINDOW_WIDTH;

@DefaultScene()
public class IntersectionSimulation extends Scene {

    public static final int BLOCK_WIDTH = 60;
    public static final int BLOCK_HEIGHT = 60;

    private final SimulationMenu simulationMenu = new SimulationMenu();

    public List<GameObject> vehiclesToAdd = new ArrayList<>();

    public static boolean debugging = false;

    @Override
    public void loadResources() {
        setBackdrop(Color.WHITE);
        AssetPool.indexFont("default_font", "assets/samples/fonts/default_font.ttf", 16);
        AssetPool.indexBufferedImage("gear_icon", "assets/samples/textures/gear_icon.png");
    }

    @Override
    public void start() {
        addGameObject(new GameObject("menu_container", new Transform(new Vector3f(0, 0, 10), new Vector2f(WINDOW_WIDTH, WINDOW_HEIGHT)), simulationMenu));
        addGameObject(StreetPrefabs.fabricateIntersection());
    }

    @Override
    public void update(double dt) {
        updateSceneGameObjects(dt);
        getGameObjects().addAll(vehiclesToAdd);
        vehiclesToAdd.clear();

        if (KeyManager.onPress(KeyEvent.VK_F3))
            debugging = !debugging;
    }

    @Override
    public void render(Graphics g) {
        renderSceneGameObjects(g);
    }

}
