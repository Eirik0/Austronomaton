package aus.utils;

public class MathUtils {
    public static int makeInt(double x) {
        return (int) Math.round(x);
    }

    public static double distance(double x0, double y0, double x1, double y1) {
        double xDist = x1 - x0;
        double yDist = y1 - y0;

        return Math.sqrt(xDist * xDist + yDist * yDist);
    }

    public static double areaOfOverlap(double x1, double y1, double r1, double x2, double y2, double r2) {
        // http://mathworld.wolfram.com/Circle-CircleIntersection.html
        // http://stackoverflow.com/questions/4247889/area-of-intersection-between-two-circles

        double d = distance(x1, y1, x2, y2);

        double part1 = r1 * r1 * Math.acos((d * d + r1 * r1 - r2 * r2) / (2 * d * r1));
        double part2 = r2 * r2 * Math.acos((d * d + r2 * r2 - r1 * r1) / (2 * d * r2));
        double part3 = 0.5 * Math.sqrt((-d + r1 + r2) * (d + r1 - r2) * (d - r1 + r2) * (d + r1 + r2));

        double ret = part1 + part2 - part3;

        // TODO something better
        if (Double.isNaN(ret)) {
            return Math.PI * r2 * r2;
        }

        return ret;
    }

    public static double getRadius(double mass, double density) {
        return Math.sqrt(mass / (Math.PI * density));
    }

    public static double getDensity(double mass, double radius) {
        return mass / (Math.PI * radius * radius);
    }
}
