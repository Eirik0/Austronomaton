package aus.main;

import aus.space.CelestialSystem;
import gt.component.ComponentCreator;
import gt.component.MouseTracker;
import gt.gameentity.CartesianSpace;
import gt.gameentity.IGraphics;
import gt.gameloop.TimeConstants;
import gt.gamestate.GameState;
import gt.gamestate.UserInput;

public class SpaceGameState implements GameState {
    private final CelestialSystem currentSystem;

    private final MouseTracker mouseTracker;
    private final CartesianSpace cs;

    private boolean leftButtonPressed = false;
    private int mouseX = 0;
    private int mouseY = 0;
    private double mouseWheelRotation = 0;

    public SpaceGameState(MouseTracker mouseTracker) {
        this.mouseTracker = mouseTracker;

        int width = ComponentCreator.DEFAULT_WIDTH;
        int height = ComponentCreator.DEFAULT_HEIGHT;
        cs = new CartesianSpace(width, height, 0, 0, width, height);
        currentSystem = CelestialSystem.newRandomSystem(cs);
    }

    @Override
    public void update(double dt) {
        currentSystem.update(dt / TimeConstants.NANOS_PER_SECOND);
    }

    @Override
    public void drawOn(IGraphics g) {
        // Background
        g.fillRect(0, 0, cs.getImageWidth(), cs.getImageHeight(), ComponentCreator.backgroundColor());

        currentSystem.drawOn(g);
    }

    @Override
    public void setSize(int width, int height) {
        cs.setSize(width, height);
    }

    @Override
    public void handleUserInput(UserInput input) {
        switch (input) {
        case MOUSE_MOVED:
            if (leftButtonPressed) {
                cs.move(mouseX - mouseTracker.mouseX, mouseY - mouseTracker.mouseY);
            }
            mouseX = mouseTracker.mouseX;
            mouseY = mouseTracker.mouseY;
            break;
        case MOUSE_WHEEL_MOVED:
            double wheelDelta = mouseWheelRotation - mouseTracker.wheelRotation;
            cs.zoom(0.05 * wheelDelta, mouseX, mouseY);
            mouseWheelRotation = mouseTracker.wheelRotation;
            break;
        case LEFT_BUTTON_PRESSED:
            leftButtonPressed = true;
            break;
        case LEFT_BUTTON_RELEASED:
            leftButtonPressed = false;
            break;
        }
    }
}
