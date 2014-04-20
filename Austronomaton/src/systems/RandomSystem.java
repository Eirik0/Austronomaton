package systems;

import java.awt.Color;
import java.util.Random;

import space.CelestialBody;
import space.CelestialSystem;

public class RandomSystem extends CelestialSystem {
	Random random = new Random();

	public RandomSystem() {
		CelestialBody sun = new CelestialBody(1000, 500, 18000, 25);
		sun.setColor(Color.BLACK);

		addStar(sun);
		int numOfPlanets = 60;
		for (int x = 0; x < numOfPlanets; ++x) {
			int mass = random.nextInt(200) + 300;
			int xPos = random.nextInt(2000);
			int yPos = random.nextInt(1000);
			CelestialBody planet = new CelestialBody(xPos, yPos, mass, mass / 30);
			int vx = random.nextInt(2) + 2;
			int vy = -random.nextInt(6) + 6;
			if (xPos > 1000) {
				vy *= -1;
			}
			if (yPos > 500) {
				vx *= -1;
			}
			planet.setVelocity(vx, vy);
			planet.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
			addPlanetLike(planet);

			// add moon
			if (random.nextInt(6) == 5) {
				CelestialBody moon = new CelestialBody(xPos + mass / 30, yPos - mass / 30, mass / 10, mass / 50);
				moon.setVelocity(vx, vy);
				moon.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
				addPlanetLike(moon);
			}
		}
	}
}
