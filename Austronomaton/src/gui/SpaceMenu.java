package gui;

import java.awt.*;
import java.util.*;
import java.util.List;

public class SpaceMenu {
	private static final String TEST = "Test";

	private static final int MENU_WIDTH = 200;
	private static final int MENU_HEIGHT = 50;

	Austronomaton astro;

	List<String> menuItems = new ArrayList<>();

	SpaceMenu(Austronomaton astro) {
		this.astro = astro;

		menuItems.add(TEST);
	}

	void drawOn(Graphics2D g, int panelWidth, int panelHeight) {
		int mouseY = astro.mouseY;
		int mouseX = astro.mouseX;

		int numberOfItems = menuItems.size();
		int menuSpacing = panelHeight / (numberOfItems + 1);

		for (int i = 0; i < numberOfItems; ++i) {
			g.setColor(Color.BLACK);
			int x0 = (panelWidth - MENU_WIDTH) / 2;
			int y0 = menuSpacing * (i + 1) - MENU_HEIGHT / 2;

			g.fillRect(x0, y0, MENU_WIDTH, MENU_HEIGHT);

			// y seems less likely than x
			if (mouseY >= y0 && mouseY <= y0 + MENU_HEIGHT && mouseX >= x0 && mouseX <= x0 + MENU_WIDTH) {
				g.setColor(Color.GREEN);
			} else {
				g.setColor(Color.RED);
			}

			g.drawRect(x0, y0, MENU_WIDTH, MENU_HEIGHT);

			String name = menuItems.get(i);

			int stringWidth = g.getFontMetrics().stringWidth(name);
			int stringHeight = g.getFontMetrics().getHeight();

			g.drawString(name, x0 + (MENU_WIDTH - stringWidth) / 2, y0 + (MENU_HEIGHT + stringHeight) / 2);
		}
	}
}