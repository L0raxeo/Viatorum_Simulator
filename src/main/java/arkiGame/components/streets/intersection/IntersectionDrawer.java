package arkiGame.components.streets.intersection;

import l0raxeo.arki.engine.components.Component;
import org.joml.Vector4i;

import java.awt.*;

public class IntersectionDrawer extends Component {


    private final int NUMBER_OF_AVENUES = 4;
    private final int LINES_PER_AVENUE = 3;
    private Vector4i[] streetLines =
            new Vector4i[NUMBER_OF_AVENUES * LINES_PER_AVENUE];

    @Override
    public void start() {
        for (int a = 0; a < NUMBER_OF_AVENUES; a++) {

        }
    }

//    private Vector4i generateStreetLine(int avenue) {
//
//    }

    @Override
    public void render(Graphics g) {
        for (Vector4i streetLine : streetLines) {
            g.drawLine(streetLine.x, streetLine.y, streetLine.w, streetLine.z);
        }
    }

}
