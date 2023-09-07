package l0raxeo.arki.engine.gameObjects.prefabs;

import l0raxeo.arki.engine.components.Component;
import l0raxeo.arki.engine.gameObjects.GameObject;
import l0raxeo.arki.engine.gameObjects.Transform;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Prefabs {

    private Prefabs() {}

    public static GameObject generate(String name, Vector3f pos, Vector2f size, Component... comps)
    {
        GameObject go = new GameObject(name, new Transform(pos, size));

        for (Component c : comps)
            if (c != null)
                go.addComponent(c);

        return go;
    }

    public static GameObject generate(String name, Vector3f pos, Vector2f size, float rotation, Component... comps)
    {
        GameObject go = new GameObject(name, new Transform(pos, size, rotation));

        for (Component c : comps)
            if (c != null)
                go.addComponent(c);

        return go;
    }

}
