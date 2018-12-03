package aus.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
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
