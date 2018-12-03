package gui;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class SpacePanel extends JPanel {
	BufferedImage image;
	Graphics2D graphics;

	public SpacePanel() {
		setPreferredSize(new Dimension(Austronomaton.DEFAULT_WIDTH, Austronomaton.DEFAULT_HEIGHT));
	}

	void draw(BufferedImage toDraw, int width, int height) {
		if (image == null || image.getWidth() != width || image.getHeight() != height) {
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			graphics = image.createGraphics();
		}

		graphics.drawImage(toDraw, 0, 0, null);
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, null);
	}

}
