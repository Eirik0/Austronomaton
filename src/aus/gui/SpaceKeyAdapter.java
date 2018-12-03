package gui;

import java.awt.event.*;

public class SpaceKeyAdapter extends KeyAdapter {
	Austronomaton astro;

	public SpaceKeyAdapter(Austronomaton astro) {
		this.astro = astro;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

		switch (keyCode) {
		case KeyEvent.VK_ESCAPE:
			astro.togglePause();
			break;
		}
	}
}
