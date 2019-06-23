package aus.space;

import java.awt.Color;
import java.util.List;

import aus.utils.MathUtils;
import gt.gameentity.CartesianSpace;
import gt.gameentity.IGraphics;

public class CelestialBody implements SpaceObject {
    protected final CartesianSpace cs;

    protected double x;
    protected double y;
    protected double velX = 0;
    protected double velY = 0;
    protected double mass;
    protected double radius;
    protected final Color color;

    public CelestialBody(CartesianSpace cs, double x0, double y0, double mass, double radius, Color color) {
        this.cs = cs;

        x = x0;
        y = y0;
        this.mass = mass;
        this.radius = radius;
        this.color = color;
    }

    public void setVelocity(double vx, double vy) {
        velX = vx;
        velY = vy;
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
    public void update(double dt) {
        x += velX * dt;
        y += velY * dt;
    }

    @Override
    public void drawOn(IGraphics g) {
        g.fillCircle(cs.getImageX(x), cs.getImageY(y), cs.getImageWidth(radius), color);
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
}
