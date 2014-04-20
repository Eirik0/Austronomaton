package space;

import java.awt.Graphics2D;

public interface SpaceObject {
	public double getX();

	public double getY();

	public double getVelX();

	public double getVelY();

	public double getMass();
	
	public void drawOn(Graphics2D g, int offsetX, int offsetY);

	public void passTime(double dt);
}
