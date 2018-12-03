package aus.space;

import java.awt.Graphics2D;

public interface SpaceObject {
    double getX();

    double getY();

    double getVelX();

    double getVelY();

    double getMass();

    void drawOn(Graphics2D g, int offsetX, int offsetY);

    void passTime(double dt);
}
