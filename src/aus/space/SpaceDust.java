package aus.space;

import java.awt.Color;

import gt.gameentity.CartesianSpace;
import gt.gameentity.IGraphics;

public class SpaceDust extends CelestialBody {
    private long lifeSpan = 0;

    public SpaceDust(CartesianSpace cs, double x0, double y0, double mass, double radius, Color color) {
        super(cs, x0, y0, mass, radius, color);
    }

    @Override
    public void update(double dt) {
        super.update(dt);
        if ((velX * velX + velY * velY) > 1000) {
            ++lifeSpan;
        }
    }

    @Override
    public void drawOn(IGraphics g) {
        g.setColor(color);
        double x1 = cs.getImageX(x);
        double y1 = cs.getImageY(y);
        g.drawLine(x1, y1, cs.getImageX(x + velX / 150), cs.getImageY(y + velY / 150));
    }

    public boolean shouldRemoveAfter(long n) {
        return lifeSpan > n;
    }
}
