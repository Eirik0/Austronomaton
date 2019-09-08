package aus.main;

import gt.component.GamePanel;
import gt.component.MainFrame;
import gt.gamestate.GameStateManager;

public class AustronomatonMain {
    private static final String TITLE = "Austronomaton";

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame(TITLE);
        GamePanel mainPanel = mainFrame.getGamePanel();

        GameStateManager gameStateManager = mainPanel.getGameStateManager();
        gameStateManager.setGameState(new SpaceGameState(gameStateManager.getMouseTracker()));

        mainFrame.show();
    }
}
