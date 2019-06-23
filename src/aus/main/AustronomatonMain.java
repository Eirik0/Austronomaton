package aus.main;

import java.awt.Dimension;

import gt.component.ComponentCreator;
import gt.component.GamePanel;
import gt.component.MainFrame;
import gt.gamestate.GameStateManager;

public class AustronomatonMain {
    private static final String TITLE = "Austronomaton";

    public static void main(String[] args) {
        ComponentCreator.setCrossPlatformLookAndFeel();

        GamePanel mainPanel = new GamePanel("Austronomaton");
        mainPanel.setPreferredSize(new Dimension(ComponentCreator.DEFAULT_WIDTH, ComponentCreator.DEFAULT_HEIGHT));

        GameStateManager gameStateManager = mainPanel.getGameStateManager();
        gameStateManager.setGameState(new SpaceGameState(gameStateManager.getMouseTracker()));

        MainFrame mainFrame = new MainFrame(TITLE, mainPanel);

        mainFrame.show();
    }
}
