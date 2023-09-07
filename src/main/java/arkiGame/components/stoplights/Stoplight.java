package arkiGame.components.stoplights;

import java.awt.*;

public enum Stoplight
{

    GREEN(new Color(100,255,100)),
    RED(new Color(255, 100, 100)),
    YELLOW(Color.YELLOW);

    private final Color drawableColor;

    Stoplight(Color color) {
        this.drawableColor = color;
    }

    public Color getDrawableColor() {
        return this.drawableColor;
    }

}
