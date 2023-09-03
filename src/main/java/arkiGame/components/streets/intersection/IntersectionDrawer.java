package arkiGame.components.streets.intersection;

import l0raxeo.arki.engine.components.Component;
import l0raxeo.arki.renderer.AppWindow;
import org.joml.Vector4i;

import java.awt.*;

import static arkiGame.scenes.IntersectionSimulation.BLOCK_HEIGHT;
import static arkiGame.scenes.IntersectionSimulation.BLOCK_WIDTH;

public class IntersectionDrawer extends Component {

    private final int NUMBER_OF_AVENUES = 4;
    private final int LINES_PER_AVENUE = 3;

    private Vector4i[] streetLines =
            new Vector4i[NUMBER_OF_AVENUES * LINES_PER_AVENUE];

    @Override
    public void start() {
        int streetLinesIndex = 0;
        for (int avenue = 1; avenue <= NUMBER_OF_AVENUES; avenue++) {
            for (int line = 1; line <= LINES_PER_AVENUE; line++) {
                streetLines[streetLinesIndex] = generateStreetLine(avenue, line);
                streetLinesIndex++;
            }
        }
    }

    private Vector4i generateStreetLine(int avenue, int line) {
        boolean isVertical = avenue % 2 != 0;

        int x1;
        int y1;

        int x2;
        int y2;

        if (isVertical) {
            x1 = ((AppWindow.WINDOW_WIDTH / 2) - BLOCK_WIDTH) + ((line - 1) * BLOCK_WIDTH);
            y1 = avenue == 1 ? 0 : (BLOCK_HEIGHT * 7);

            x2 = x1;
            y2 = y1 + (BLOCK_HEIGHT * 5);
        }
        else {
            x1 = avenue == 2 ? AppWindow.WINDOW_WIDTH - (BLOCK_WIDTH * 5) : 0;
            y1 = ((AppWindow.WINDOW_HEIGHT / 2) - (BLOCK_HEIGHT * 2)) + (line * BLOCK_HEIGHT);

            x2 = x1 + (BLOCK_WIDTH * 5);
            y2 = y1;
        }

        return new Vector4i(x1, y1, x2, y2);
    }

    @Override
    public void render(Graphics g) {
        for (Vector4i streetLine : streetLines) {
            if (streetLine != null) {
                g.setColor(Color.BLACK);
                g.drawLine(streetLine.x, streetLine.y, streetLine.z, streetLine.w);
            }
        }
    }

}
