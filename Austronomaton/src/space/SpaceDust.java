package space;

import java.awt.Graphics2D;

import utils.MathUtils;

public class SpaceDust extends CelestialBody {
	private long lifeSpan = 0;

	public SpaceDust(double x0, double y0, double mass, double radius) {
		super(x0, y0, mass, radius);
	}

	@Override
	public void passTime(double dt) {
		super.passTime(dt);
		if ((velX * velX + velY * velY) > 1000) {
			++lifeSpan;
		}
	}

	@Override
	public void drawOn(Graphics2D g, int offsetX, int offsetY) {
		g.setColor(color);
		int x1 = MathUtils.makeInt(x) - offsetX;
		int y1 = MathUtils.makeInt(y) - offsetY;
		g.drawLine(x1, y1, MathUtils.makeInt(x + velX / 150) - offsetX, MathUtils.makeInt(y + velY / 150) - offsetY);
	}

	public boolean shouldRemoveAfter(long n) {
		return lifeSpan > n;
	}

}
