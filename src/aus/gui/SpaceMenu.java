package gui;

import java.awt.Graphics2D;
import java.util.*;

public class SpaceMenu {
	static final String START = "Start";

	private static final int MENU_WIDTH = 200;
	private static final int MENU_HEIGHT = 50;

	Austronomaton astro;

	List<SpaceMenuItem> menuItems = new ArrayList<>();

	SpaceMenu(Austronomaton astro) {
		this.astro = astro;

		int x0 = (Austronomaton.DEFAULT_WIDTH - MENU_WIDTH) / 2;
		int y0 = (Austronomaton.DEFAULT_HEIGHT - MENU_HEIGHT) / 2;
		menuItems.add(new SpaceMenuItem(START, x0, y0, MENU_WIDTH, MENU_HEIGHT));
	}

	String getSelection(int mouseX, int mouseY) {
		for (SpaceMenuItem item : menuItems) {
			if (item.contains(mouseX, mouseY)) {
				return item.getTitle();
			}
		}

		return null;
	}

	void drawOn(Graphics2D g) {
		int mouseY = astro.mouseY;
		int mouseX = astro.mouseX;

		for (SpaceMenuItem item : menuItems) {
			// highlight the menu item if the mouse's x, y is contained within its bounds
			item.setSelected(item.contains(mouseX, mouseY));
			item.drawOn(g);
		}
	}
}