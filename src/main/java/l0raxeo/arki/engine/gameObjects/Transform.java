package l0raxeo.arki.engine.gameObjects;

import l0raxeo.arki.renderer.AppWindow;
import l0raxeo.arki.engine.scenes.Camera;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Transform
{

    private Rectangle bounds;
    public Vector2f noCamScreenPosition;
    public Vector2f scale;
    private int zIndex = 0;
    public float rotation;

    public Transform(Vector3f position, Vector2f scale, float rotation)
    {
        init(position, scale, rotation);
    }

    public Transform(Vector3f position, Vector2f scale)
    {
        init(position, scale, 0);
    }

    public void init(Vector3f worldPosition, Vector2f scale, float rotation)
    {
        // world to screen coordinates
        this.noCamScreenPosition = new Vector2f(worldPosition.x, AppWindow.WINDOW_HEIGHT - worldPosition.y);
        this.scale = scale;
        this.rotation = rotation;
        this.zIndex = (int) worldPosition.z;

        this.bounds = new Rectangle((int) getScreenPosition().x, (int) getScreenPosition().y, 60, 60);
    }

    public boolean boundsContain(Vector2i point) {
        bounds.x = (int) getScreenPosition().x;
        bounds.y = (int) getScreenPosition().y;
        return bounds.contains(point.x, point.y);
    }

    public Vector2f worldPosition()
    {
        return new Vector2f(noCamScreenPosition.x, AppWindow.WINDOW_HEIGHT - noCamScreenPosition.y);
    }

    public Vector2f getScreenPosition()
    {
        return new Vector2f(noCamScreenPosition.x + Camera.xOffset(), noCamScreenPosition.y + Camera.yOffset());
    }

    public void setScreenPosition(Vector2f position) {
        this.noCamScreenPosition = position;
    }

    public void setScreenPosition(float x, float y) {
        this.noCamScreenPosition.x = x;
        this.noCamScreenPosition.y = y;
    }

    public Vector2f getScreenCenterPosition()
    {
        return getScreenPosition().add(scale.x / 2, scale.y / 2);
    }

    public void setWorldPosition(Vector2f worldPosition)
    {
        setWorldPosition(worldPosition.x, worldPosition.y);
    }

    public void setWorldPosition(float worldX, float worldY)
    {
        this.noCamScreenPosition.x = worldX;
        this.noCamScreenPosition.y = AppWindow.WINDOW_HEIGHT - worldY;
    }

    public void setWorldPosition(Vector3i worldPosition)
    {
        setWorldPosition(worldPosition.x, worldPosition.y);
        setzIndex(worldPosition.z);
    }

    public void setzIndex(int zIndex)
    {
        this.zIndex = zIndex;
    }

    public int getzIndex()
    {
        return zIndex;
    }

    public void move(Vector2f worldCoordinateOffset)
    {
        this.noCamScreenPosition.x += worldCoordinateOffset.x;
        this.noCamScreenPosition.y -= worldCoordinateOffset.y;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) return false;
        if (!(obj instanceof Transform t)) return false;

        return t.noCamScreenPosition.equals(this.noCamScreenPosition) && t.scale.equals(this.scale);
    }

}
