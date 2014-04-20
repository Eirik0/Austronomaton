package gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import space.CelestialSystem;
import systems.RandomSystem;

public class Main {

	public static void main(String[] args) {
		CelestialSystem system = new RandomSystem();
		SpacePanel spacePanel = new SpacePanel(system);

		JFrame spaceFrame = new JFrame("Austronomaton");

		spaceFrame.setSize(SpacePanel.DEFAULT_WIDTH, SpacePanel.DEFAULT_HEIGHT);
		spaceFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		spaceFrame.setLayout(new BorderLayout());

		spaceFrame.add(spacePanel, BorderLayout.CENTER);

		spaceFrame.setVisible(true);
		spacePanel.startGame();
	}

}
