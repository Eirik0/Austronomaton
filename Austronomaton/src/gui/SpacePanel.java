package gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

import space.CelestialSystem;

public class SpacePanel extends JPanel {

	public static int DEFAULT_WIDTH = 1600;
	public static int DEFAULT_HEIGHT = 900;

	private CelestialSystem system;
	private boolean isRunning = false;
	private SpaceMouseAdapter mouseController = new SpaceMouseAdapter();
	BufferedImage image;
	Graphics2D graphics;

	public SpacePanel(CelestialSystem system) {
		this.system = system;

		addMouseListener(mouseController);
		addMouseMotionListener(mouseController);

		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		image = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_INT_RGB);
		graphics = image.createGraphics();

		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> repaint(), 0, 15, TimeUnit.MILLISECONDS);
	}

	public void stopGame() {
		isRunning = false;
	}

	public void startGame() {
		isRunning = true;
		new Thread(() -> runGame()).start();
	}

	private void runGame() {
		double lastTime = System.nanoTime();
		double currentTime = System.nanoTime();

		while (isRunning) {
			currentTime = System.nanoTime();

			double dt = (currentTime - lastTime) / 500000000;

			system.passTime(dt);
			
			graphics.drawImage(system.getImage(getWidth(), getHeight(), mouseController.getOffsetX(), mouseController.getOffsetY()),0,0,null);
			
			lastTime = currentTime;
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, null);
	}

}
