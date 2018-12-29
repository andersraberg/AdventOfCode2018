package se.anders_raberg.adventofcode2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day25 {
    private static final int MAX_DISTANCE_WITHIN_CONSTELLATION = 3;
    private static final Logger LOGGER = Logger.getLogger(Day25.class.getName());
    private static final Pattern PATTERN = Pattern.compile("(.+),(.+),(.+),(.+)");

    private static class SpacetimePoint {
        private final long _x;
        private final long _y;
        private final long _z;
        private final long _t;

        public SpacetimePoint(long x, long y, long z, long t) {
            _x = x;
            _y = y;
            _z = z;
            _t = t;
        }

        public static SpacetimePoint parse(String str) {
            Matcher m = PATTERN.matcher(str);
            m.find();
            return new SpacetimePoint(Long.parseLong(m.group(1)), //
                    Long.parseLong(m.group(2)), //
                    Long.parseLong(m.group(3)), //
                    Long.parseLong(m.group(4)));
        }

        public long distanceTo(SpacetimePoint b) {
            return Math.abs(b._x - _x) + Math.abs(b._y - _y) + Math.abs(b._z - _z) + Math.abs(b._t - _t);
        }

        @Override
        public String toString() {
            return String.format("(%d, %d, %d, %d)", _x, _y, _z, _t);
        }
    }

    public static void run() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/input25.txt"));
        List<SpacetimePoint> remainingPoints = lines.stream().map(SpacetimePoint::parse).collect(Collectors.toList());

        int numberOfConstellations = 0;
        while (!remainingPoints.isEmpty()) {
            Set<SpacetimePoint> constellation = new HashSet<>();
            SpacetimePoint point = remainingPoints.remove(0);
            constellation.add(point);
            Set<SpacetimePoint> closePoints;
            do {
                closePoints = pointsCloseToConstellation(constellation, remainingPoints);
                constellation.addAll(closePoints);
                remainingPoints.removeAll(closePoints);
            } while (!closePoints.isEmpty());
            numberOfConstellations++;
        }
        LOGGER.info("Part 1 : Number of constellations: " + numberOfConstellations);
    }

    private static Set<SpacetimePoint> pointsCloseToConstellation(Set<SpacetimePoint> constellation,
            List<SpacetimePoint> points) {
        return points.stream() //
                .filter(p -> pointCloseToAnyInConstellation(constellation, p)) //
                .collect(Collectors.toSet());
    }

    private static boolean pointCloseToAnyInConstellation(Set<SpacetimePoint> constellation, SpacetimePoint point) {
        return constellation.stream().anyMatch(p -> p.distanceTo(point) <= MAX_DISTANCE_WITHIN_CONSTELLATION);
    }
}
