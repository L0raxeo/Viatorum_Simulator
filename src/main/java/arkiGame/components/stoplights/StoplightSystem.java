package arkiGame.components.stoplights;

import arkiGame.components.menu.SimulationMenu;
import arkiGame.components.streets.intersection.StreetPath;
import l0raxeo.arki.engine.components.Component;
import l0raxeo.arki.engine.eventSystem.EventTrigger;
import org.joml.Vector2i;

import java.awt.*;

import static arkiGame.scenes.IntersectionSimulation.BLOCK_HEIGHT;
import static arkiGame.scenes.IntersectionSimulation.BLOCK_WIDTH;

public class StoplightSystem extends Component {

    public static EventTrigger lightChange = new EventTrigger("lightChange");

    private final Stoplight[] stoplights = new Stoplight[4];
    private final StreetPath[] paths = new StreetPath[4];

    private int stoplightIntervals;
    private int stoplightTimer;

    private boolean isRunning = false;

    private void subscribeToEvents() {
        StoplightSystem.lightChange.subscribe(this, "onLightChange", Object.class, boolean.class);
        SimulationMenu.startSimulationEvent.subscribe(this, "onSimulationStart");
        SimulationMenu.stopSimulationEvent.subscribe(this, "onSimulationStop");
    }

    private void initializeStoplights() {
        for (int i = 0;i < stoplights.length; i++) {
            stoplights[i] = i % 2 == 0 ? Stoplight.RED : Stoplight.GREEN;
        }
    }

    private void initializePaths() {
        for (StreetPath path : gameObject.getComponents(StreetPath.class)) {
            if (path.getId().contains("enter")) {
                paths[Integer.parseInt(path.getId().substring(0, 1))] = path;
            }
        }
    }

    @Override
    public void start() {
        // initialize
        subscribeToEvents();
        initializeStoplights();
        initializePaths();

        // set stoplight intervals
        stoplightIntervals = 600;
    }

    @Override
    public void update(double dt) {
        if (!isRunning)
            return;

        if (stoplightTimer <= stoplightIntervals / 2) {
            lightChange.triggerEvent(this, true);

            if (stoplightTimer <= 0) {
                lightChange.triggerEvent(this, false);
                stoplightTimer = stoplightIntervals;
            }
        }

        stoplightTimer--;
    }

    @Override
    public void render(Graphics g) {
        if (!isRunning)
            return;

        for (int i = 0; i < stoplights.length; i++) {
            Vector2i curPathPos = paths[i].getBlockPosition(5);
            g.setColor(stoplights[i].getDrawableColor());
            g.fillRect(curPathPos.x - (BLOCK_WIDTH / 2), curPathPos.y - (BLOCK_HEIGHT / 2), BLOCK_WIDTH, BLOCK_HEIGHT);
        }
    }

    public Stoplight getStoplight(StreetPath streetPath) {
        for (int i = 0; i < stoplights.length; i++) {
            if (paths[i].getId().equals(streetPath.getId())) {
                return stoplights[i];
            }
        }

        return null;
    }

    public void onLightChange(Object sender, boolean changedYellow) {
        for (int i = 0; i < stoplights.length; i++) {
            stoplights[i] = changedYellow ? (stoplights[i] == Stoplight.GREEN ? Stoplight.YELLOW : stoplights[i])
                    : (stoplights[i] == Stoplight.RED ? Stoplight.GREEN : Stoplight.RED);
        }
    }

    public void onSimulationStart() {
        isRunning = true;
    }

    public void onSimulationStop() {
        isRunning = false;
        stoplightTimer = 0;
    }

}
