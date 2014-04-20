package space;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import utils.MathUtils;

public class CelestialBody implements SpaceObject {
	double x;
	double y;
	double velX = 0;
	double velY = 0;
	double mass;
	double radius;
	Color color = Color.BLACK;

	public CelestialBody(double x0, double y0, double mass, double radius) {
		x = x0;
		y = y0;
		this.mass = mass;
		this.radius = radius;
	}

	public void setVelocity(double vx, double vy) {
		velX = vx;
		velY = vy;
	}

	public void setColor(Color c) {
		this.color = c;
	}

	public double getRadius() {
		return radius;
	}

	public boolean collidesWith(CelestialBody other) {
		return MathUtils.distance(x, y, other.x, other.y) < radius + other.radius;
	}

	public void addMass(double massToAdd) {
		double density = MathUtils.getDensity(mass, radius);
		mass += massToAdd;
		radius = MathUtils.getRadius(mass, density);
	}

	public double getDensity() {
		return MathUtils.getDensity(mass, radius);
	}

	public double getOverlappingArea(CelestialBody other) {
		return MathUtils.areaOfOverlap(x, y, radius, other.x, other.y, other.radius);
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public double getVelX() {
		return velX;
	}

	@Override
	public double getVelY() {
		return velY;
	}

	@Override
	public double getMass() {
		return mass;
	}

	@Override
	public void drawOn(Graphics2D g, int offsetX, int offsetY) {
		g.setColor(color);
		g.fillOval(MathUtils.makeInt(x - radius) - offsetX, MathUtils.makeInt(y - radius) - offsetY, MathUtils.makeInt(2 * radius),
				MathUtils.makeInt(2 * radius));
	}

	public void applyForceOfGravity(List<? extends SpaceObject> spaceObjects, double dt) {
		double fX = 0;
		double fY = 0;

		for (SpaceObject spaceObj : spaceObjects) {
			if (this != spaceObj) {
				double x2 = spaceObj.getX();
				double y2 = spaceObj.getY();

				double r = MathUtils.distance(x, y, x2, y2);
				
				if (r < 0.001) {
					continue;
				}
				
				double f = mass * spaceObj.getMass() / (r * r);

				fX += f * (x2 - x) / r;
				fY += f * (y2 - y) / r;
			}
		}

		velX += (fX * dt) / mass;
		velY += (fY * dt) / mass;
	}

	@Override
	public void passTime(double dt) {
		x += velX * dt;
		y += velY * dt;
	}
}
