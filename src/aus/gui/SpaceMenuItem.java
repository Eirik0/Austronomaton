package aus.gui;

import java.awt.Color;
import java.awt.Graphics2D;

public class SpaceMenuItem {

    private String title;

    private int x0;
    private int y0;
    private int width;
    private int height;

    private boolean isSelected;

    SpaceMenuItem(String title, int x0, int y0, int width, int height) {
        this.title = title;

        this.x0 = x0;
        this.y0 = y0;
        this.width = width;
        this.height = height;
    }

    String getTitle() {
        return title;
    }

    void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    boolean contains(int x, int y) {
        return y > y0 && y < (y0 + height) && x > x0 && x < (x0 + width);
    }

    void drawOn(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(x0, y0, width, height);

        g.setColor(isSelected ? Color.GREEN : Color.RED);
        g.drawRect(x0, y0, width, height);

        int stringWidth = g.getFontMetrics().stringWidth(title);
        int stringHeight = g.getFontMetrics().getHeight();

        g.drawString(title, x0 + (width - stringWidth) / 2, y0 + (height + stringHeight) / 2);
    }
}
