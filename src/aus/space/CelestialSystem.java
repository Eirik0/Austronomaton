package space;

import java.awt.*;
import java.util.*;
import java.util.List;

import utils.MathUtils;

public abstract class CelestialSystem {
	private List<CelestialBody> celestialBodies = new ArrayList<>();
	private List<CelestialBody> stars = new ArrayList<>();
	private List<CelestialBody> planetLike = new ArrayList<>();
	private List<SpaceDust> spaceDust = new ArrayList<>();

	public CelestialSystem() {
	}

	protected void addStar(CelestialBody star) {
		stars.add(star);
		celestialBodies.add(star);
	}

	protected void addPlanetLike(CelestialBody star) {
		planetLike.add(star);
		celestialBodies.add(star);
	}

	public void passTime(double dt) {
		celestialBodies.parallelStream().forEach(cb -> cb.applyForceOfGravity(celestialBodies, dt));

		celestialBodies.parallelStream().forEach(cb -> cb.passTime(dt));

		spaceDust.parallelStream().forEach(cb -> cb.applyForceOfGravity(celestialBodies, dt));

		spaceDust.parallelStream().forEach(cb -> cb.passTime(dt));

		handleCollisions(dt);

		Iterator<SpaceDust> dustIterator = spaceDust.iterator();
		while (dustIterator.hasNext()) {
			if (dustIterator.next().shouldRemoveAfter(500)) {
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
					newSpaceDust.add(makeStarDust(cb, dust, dt));
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
		spaceDust.add(createSpaceDust(dustX, dustY, massToTake / 2, -ny * vel, nx * vel, cb2.color));
		spaceDust.add(createSpaceDust(dustX, dustY, massToTake / 2, ny * vel, -nx * vel, cb2.color));

		// TODO Make planets resist each other differently
		cb2.velX -= 0.75 * nx / cb2.mass;
		cb2.velY -= 0.75 * ny / cb2.mass;
		cb1.velX += 0.75 * nx / cb1.mass;
		cb1.velY += 0.75 * ny / cb1.mass;
	}

	private SpaceDust makeStarDust(CelestialBody star, CelestialBody cb2, double dt) {
		// Fling space dust perpendicular to the star
		double distance = MathUtils.distance(star.x, star.y, cb2.x, cb2.y);
		double nx = (cb2.x - star.x) / distance;
		double ny = (cb2.y - star.y) / distance;

		double dustX = cb2.x + nx * cb2.radius + nx;
		double dustY = cb2.y + ny * cb2.radius + ny;

		return createSpaceDust(dustX, dustY, 1, nx * 500, ny * 500, Color.WHITE);
	}

	private SpaceDust createSpaceDust(double x, double y, double mass, double vx, double vy, Color c) {
		SpaceDust dust = new SpaceDust(x, y, mass, 1);
		dust.setVelocity(vx, vy);
		dust.setColor(c);
		return dust;
	}

	public void drawOn(Graphics2D g, int width, int height, int offsetX, int offsetY) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);

		for (SpaceDust dust : spaceDust) {
			dust.drawOn(g, offsetX, offsetY);
		}

		for (CelestialBody cb : celestialBodies) {
			cb.drawOn(g, offsetX, offsetY);
		}
	}
}
