package aus.space;

import gt.gameentity.GameEntity;

public interface SpaceObject extends GameEntity {
    double getX();

    double getY();

    double getVelX();

    double getVelY();

    double getMass();
}
