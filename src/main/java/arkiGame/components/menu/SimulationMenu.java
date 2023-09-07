package arkiGame.components.menu;

import l0raxeo.arki.engine.assetFiles.AssetPool;
import l0raxeo.arki.engine.components.Component;
import l0raxeo.arki.engine.eventSystem.EventTrigger;
import l0raxeo.arki.engine.input.keyboard.KeyManager;
import l0raxeo.arki.engine.ui.GuiLayer;
import l0raxeo.arki.engine.ui.GuiText;
import l0raxeo.arki.engine.ui.components.GuiButton;
import l0raxeo.arki.engine.ui.components.GuiComponent;
import l0raxeo.arki.engine.ui.components.GuiImageButton;
import l0raxeo.arki.engine.ui.components.GuiTextField;
import l0raxeo.arki.renderer.AppWindow;
import org.joml.Vector2i;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import static l0raxeo.arki.renderer.AppWindow.WINDOW_HEIGHT;
import static l0raxeo.arki.renderer.AppWindow.WINDOW_WIDTH;

public class SimulationMenu extends Component {

    public static EventTrigger startSimulationEvent = new EventTrigger("start_simulation");
    public static EventTrigger stopSimulationEvent = new EventTrigger("stop_simulation");

    private boolean configuring = false;
    private boolean simulationRunning = false;

    @Override
    public void start() {
        GuiLayer.addGuiComponent(new GuiImageButton(
                "config_btn",
                new Vector2i(WINDOW_WIDTH - 96, 32),
                new Vector2i(64, 64),
                3,
                new Color[] {Color.GRAY, Color.DARK_GRAY},
                new BufferedImage[] {AssetPool.getBufferedImage("gear_icon"), AssetPool.getBufferedImage("gear_icon")},
                () -> {
                    toggleConfig();
                }
        ));

        GuiLayer.addGuiComponent(new GuiTextField(
                "ups_cap",
                "60",
                new Vector2i(256, 128),
                new Vector2i(128, 32),
                AssetPool.getFont("default_font"),
                new Color[] {Color.DARK_GRAY, Color.GRAY},
                () -> {
                    GuiTextField upsCapField = ((GuiTextField) GuiLayer.getSelectedComponent());
                    try {
                        String upsFieldText = upsCapField.getText();
                        AppWindow.setUpsCapDuringRuntime(Integer.parseInt(upsFieldText));
                    } catch (NumberFormatException e) {
                        upsCapField.setText("60");
                    }
                }
        ));

        GuiLayer.addGuiComponent(new GuiTextField(
                "fps_cap",
                "60",
                new Vector2i(256, 176),
                new Vector2i(128, 32),
                AssetPool.getFont("default_font"),
                new Color[] {Color.DARK_GRAY, Color.GRAY},
                () -> {
                    GuiTextField fpsCapField = ((GuiTextField) GuiLayer.getSelectedComponent());
                    try {
                        String fpsFieldText = fpsCapField.getText();
                        AppWindow.setFpsCapDuringRuntime(Integer.parseInt(fpsFieldText));
                    } catch (NumberFormatException e) {
                        fpsCapField.setText("60");
                    }
                }
        ));

        GuiLayer.addGuiComponent(new GuiButton(
                "start_stop_sim",
                new Vector2i(WINDOW_WIDTH / 2 - 64, WINDOW_HEIGHT - 64),
                new Vector2i(128, 32),
                "Start",
                AssetPool.getFont("default_font"),
                new Color[] {Color.GRAY, Color.DARK_GRAY},
                true,
                () -> {
                    GuiButton startStopSimBtn = ((GuiButton) GuiLayer.getGuiComponent("start_stop_sim"));

                    if (simulationRunning) {
                        simulationRunning = false;
                        startStopSimBtn.setText("start");
                        stopSimulationEvent.triggerEvent();
                    }
                    else {
                        simulationRunning = true;
                        startStopSimBtn.setText("stop");
                        startSimulationEvent.triggerEvent();
                    }
                }
        ));

        disableConfigMenu();
    }

    private void enableConfigMenu() {
        GuiLayer.getAllGuiComponents().forEach(GuiComponent::enable);
    }

    private void disableConfigMenu() {
        for (GuiComponent guiComponent : GuiLayer.getAllGuiComponents()) {
            if (!guiComponent.name.equals("config_btn")) {
                guiComponent.disable();
            }
        }
    }

    @Override
    public void render(Graphics g) {
        if (configuring) {
            g.setColor(new Color(255, 255, 255, 200));
            g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

            GuiText.drawString(
                    g,
                    "Ticks/Seconds",
                    new Vector2i(64, 150),
                    false,
                    Color.BLACK,
                    AssetPool.getFont("default_font")
            );

            GuiText.drawString(
                    g,
                    "Frames/Seconds",
                    new Vector2i(64, 196),
                    false,
                    Color.BLACK,
                    AssetPool.getFont("default_font")
            );
        }
    }

    @Override
    public void update(double dt) {
        if (KeyManager.onPress(KeyEvent.VK_ESCAPE))
            toggleConfig();
    }

    private void toggleConfig() {
        configuring = !configuring;
        if (configuring)
            enableConfigMenu();
        else disableConfigMenu();
    }

}
