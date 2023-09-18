package arkiGame.components.debugging;

import l0raxeo.arki.engine.components.Component;

import java.awt.*;

public class Highlighter extends Component {

    @Override
    public void render(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect(gameObject.transform.getBounds().x, gameObject.transform.getBounds().y, gameObject.transform.getBounds().width, gameObject.transform.getBounds().height);
    }
}
