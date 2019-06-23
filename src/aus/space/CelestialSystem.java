package aus.space;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import aus.utils.MathUtils;
import gt.gameentity.CartesianSpace;
import gt.gameentity.GameEntity;
import gt.gameentity.IGraphics;

public class CelestialSystem implements GameEntity {
    private final CartesianSpace cs;

    private final List<CelestialBody> celestialBodies = new ArrayList<>();
    private final List<CelestialBody> stars = new ArrayList<>();
    private final List<CelestialBody> planetLike = new ArrayList<>();
    private final List<SpaceDust> spaceDust = new ArrayList<>();

    public CelestialSystem(CartesianSpace cs) {
        this.cs = cs;
    }

    public void addStar(CelestialBody star) {
        stars.add(star);
        celestialBodies.add(star);
    }

    public void addPlanetLike(CelestialBody star) {
        planetLike.add(star);
        celestialBodies.add(star);
    }

    public static CelestialSystem newRandomSystem(CartesianSpace cs) {
        Random random = new Random();

        CelestialSystem system = new CelestialSystem(cs);

        CelestialBody sun = new CelestialBody(cs, 1000, 500, 18000, 25, Color.BLACK);
        system.addStar(sun);

        int numOfPlanets = 100;
        for (int x = 0; x < numOfPlanets; ++x) {
            int mass = random.nextInt(200) + 300;
            int xPos = random.nextInt(2000);
            int yPos = random.nextInt(1000);
            Color planetColor = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
            CelestialBody planet = new CelestialBody(cs, xPos, yPos, mass, mass / 30, planetColor);
            int vx = random.nextInt(4) + 4;
            int vy = -random.nextInt(8) + 8;
            if (xPos > 1000) {
                vy *= -1;
            }
            if (yPos > 500) {
                vx *= -1;
            }
            planet.setVelocity(vx, vy);
            system.addPlanetLike(planet);

            // add moon
            if (random.nextInt(3) == 2) {
                Color moonColor = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
                CelestialBody moon = new CelestialBody(cs, xPos + mass / 30, yPos - mass / 30, mass / 10, mass / 50, moonColor);
                moon.setVelocity(vx, vy);
                system.addPlanetLike(moon);
            }
        }
        return system;
    }

    @Override
    public void update(double dt) {
        celestialBodies.parallelStream().forEach(cb -> cb.applyForceOfGravity(celestialBodies, dt));

        celestialBodies.parallelStream().forEach(cb -> cb.update(dt));

        spaceDust.parallelStream().forEach(cb -> cb.applyForceOfGravity(celestialBodies, dt));

        spaceDust.parallelStream().forEach(cb -> cb.update(dt));

        handleCollisions(dt);

        Iterator<SpaceDust> dustIterator = spaceDust.iterator();
        while (dustIterator.hasNext()) {
            if (dustIterator.next().shouldRemoveAfter(50000)) {
                dustIterator.remove();
            }
        }
    }

    private void handleCollisions(double dt) {
        // Dust colliding with stars
        List<SpaceDust> newSpaceDust = new ArrayList<>();
        Iterator<SpaceDust> dustIterator = spaceDust.iterator();
        while (dustIterator.hasNext()) {
            SpaceDust dust = dustIterator.next();

            for (CelestialBody cb : stars) {
                if (dust.collidesWith(cb)) {
                    newSpaceDust.add(makeStarDust(cs, cb, dust, dt));
                    cb.addMass(dust.getMass());

                    dustIterator.remove();
                    break;
                }
            }
        }
        spaceDust.addAll(newSpaceDust);

        // TODO star v star

        // Planet colliding with stars
        List<CelestialBody> planetsDestroyedByStars = new ArrayList<>();
        for (CelestialBody star : stars) {
            for (CelestialBody planet : planetLike) {
                if (star.collidesWith(planet)) {
                    double overlap = star.getOverlappingArea(planet);
                    double massToTake = planet.getDensity() * overlap;

                    star.addMass(massToTake);
                    planet.addMass(-massToTake);

                    if (planet.getRadius() < 1) {
                        planetsDestroyedByStars.add(planet);
                    }
                }
            }
        }
        planetLike.removeAll(planetsDestroyedByStars);
        celestialBodies.removeAll(planetsDestroyedByStars);

        List<CelestialBody> planetsDestroyedByPlanets = new ArrayList<>();
        int numOfBodies = planetLike.size();
        for (int i = 0; i < numOfBodies; ++i) {
            CelestialBody cb1 = planetLike.get(i);
            for (int j = i + 1; j < numOfBodies; ++j) {
                CelestialBody cb2 = planetLike.get(j);
                if (cb1.collidesWith(cb2)) {
                    if (cb1.mass > cb2.mass) {
                        makeDust(cb1, cb2, dt);
                        if (cb2.getRadius() < 1) {
                            planetsDestroyedByPlanets.add(cb2);
                        }
                    } else {
                        makeDust(cb2, cb1, dt);
                        if (cb1.getRadius() < 1) {
                            planetsDestroyedByPlanets.add(cb1);
                        }
                    }
                }
            }
        }
        planetLike.removeAll(planetsDestroyedByPlanets);
        celestialBodies.removeAll(planetsDestroyedByPlanets);
    }

    private void makeDust(CelestialBody cb1, CelestialBody cb2, double dt) {
        double overlap = cb1.getOverlappingArea(cb2);
        double massToTake = cb2.getDensity() * overlap;
        double distance = MathUtils.distance(cb1.x, cb1.y, cb2.x, cb2.y);
        double nx = (cb1.x - cb2.x) / distance;
        double ny = (cb1.y - cb2.y) / distance;

        double dustX = cb2.x + nx * cb2.radius;
        double dustY = cb2.y + ny * cb2.radius;
        cb2.addMass(-massToTake);

        double vel = Math.sqrt(cb2.velX * cb2.velX + cb2.velY * cb2.velY);

        // Create two dust tangent to the collision
        spaceDust.add(createSpaceDust(cs, dustX, dustY, massToTake / 2, -ny * vel, nx * vel, cb2.color));
        spaceDust.add(createSpaceDust(cs, dustX, dustY, massToTake / 2, ny * vel, -nx * vel, cb2.color));

        // TODO Make planets resist each other differently
        cb2.velX -= 0.75 * nx / cb2.mass;
        cb2.velY -= 0.75 * ny / cb2.mass;
        cb1.velX += 0.75 * nx / cb1.mass;
        cb1.velY += 0.75 * ny / cb1.mass;
    }

    private static SpaceDust makeStarDust(CartesianSpace cs, CelestialBody star, CelestialBody cb2, double dt) {
        // Fling space dust perpendicular to the star
        double distance = MathUtils.distance(star.x, star.y, cb2.x, cb2.y);
        double nx = (cb2.x - star.x) / distance;
        double ny = (cb2.y - star.y) / distance;

        double dustX = cb2.x + nx * cb2.radius + nx;
        double dustY = cb2.y + ny * cb2.radius + ny;

        return createSpaceDust(cs, dustX, dustY, 1, nx * 500, ny * 500, Color.WHITE);
    }

    private static SpaceDust createSpaceDust(CartesianSpace cs, double x, double y, double mass, double vx, double vy, Color c) {
        SpaceDust dust = new SpaceDust(cs, x, y, mass, 1, c);
        dust.setVelocity(vx, vy);
        return dust;
    }

    @Override
    public void drawOn(IGraphics g) {
        for (SpaceDust dust : spaceDust) {
            dust.drawOn(g);
        }

        for (CelestialBody cb : celestialBodies) {
            cb.drawOn(g);
        }
    }
}
