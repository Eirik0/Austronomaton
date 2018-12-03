package gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.*;

import javax.swing.JFrame;

import space.CelestialSystem;
import systems.RandomSystem;

public class Austronomaton {
	private static final String NAME = "Austronomaton";

	static final int DEFAULT_WIDTH = 1600;
	static final int DEFAULT_HEIGHT = 900;

	int mouseX = 0;
	int mouseY = 0;

	boolean isPaused = false;

	SpaceKeyAdapter spaceKeyAdapter;
	SpaceMouseAdapter spaceMouseAdapter;

	SpacePanel spacePanel;

	SpaceMenu spaceMenu;

	private CelestialSystem currentSystem;

	private Austronomaton() {
		currentSystem = new RandomSystem();

		spaceKeyAdapter = new SpaceKeyAdapter(this);
		spaceMouseAdapter = new SpaceMouseAdapter(this);

		spacePanel = new SpacePanel();
		spacePanel.addMouseListener(spaceMouseAdapter);
		spacePanel.addMouseMotionListener(spaceMouseAdapter);

		spaceMenu = new SpaceMenu(this);
	}

	void togglePause() {
		if (!isPaused) {
			spaceMouseAdapter.stopDragging();
		}

		isPaused = !isPaused;
	}

	void setMouseXY(int mouseX, int mouseY) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
	}

	private void start() {
		int width = spacePanel.getWidth();
		int height = spacePanel.getHeight();

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();

		// Repaint space panel forever
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> spacePanel.repaint(), 0, 15, TimeUnit.MILLISECONDS);

		// Start game loop
		new Thread(() -> runGameLoop(image, graphics, width, height)).start();
	}

	private void runGameLoop(BufferedImage image, Graphics2D graphics, int width, int height) {
		double lastTime = System.nanoTime();
		double currentTime = System.nanoTime();

		while (true) {
			currentTime = System.nanoTime();

			double dt = (currentTime - lastTime) / 500000000;

			if (!isPaused) {
				currentSystem.passTime(dt);

				spaceMouseAdapter.updateScreenMomentum();
			}

			currentSystem.drawOn(graphics, width, height, spaceMouseAdapter.getOffsetX(), spaceMouseAdapter.getOffsetY());

			// draw the menu on top of the current system
			if (isPaused) {
				spaceMenu.drawOn(graphics);
			}

			spacePanel.draw(image, width, height);

			lastTime = currentTime;
		}
	}

	void handleMouseReleased() {
		// only called if the game is paused
		String selection = spaceMenu.getSelection(mouseX, mouseY);
		if (selection == null) {
			return;
		} else if (selection == SpaceMenu.START) {
			currentSystem = new RandomSystem();
			isPaused = false;
		}
	}

	public static void main(String[] args) {
		Austronomaton astro = new Austronomaton();

		JFrame spaceFrame = new JFrame(NAME);

		spaceFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		spaceFrame.setLayout(new BorderLayout());

		spaceFrame.add(astro.spacePanel);
		spaceFrame.pack();

		spaceFrame.setResizable(false);
		spaceFrame.setVisible(true);

		spaceFrame.addKeyListener(astro.spaceKeyAdapter);

		astro.start();
	}

}
