package se.anders_raberg.adventofcode2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Day6 {
    private static final int MAX_SUM_OF_DIST = 10000;
    private static final int GRID_SIZE = 400;
    private static final Logger LOGGER = Logger.getLogger(Day6.class.getName());

    private static class Coord {
        private final int _x;
        private final int _y;

        public Coord(int x, int y) {
            _x = x;
            _y = y;
        }

        public static Coord parse(String str) {
            String[] split = str.trim().split(", ");
            return new Coord(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        }

        @Override
        public int hashCode() {
            return Objects.hash(_x, _y);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            Coord other = (Coord) obj;
            return _x == other._x && _y == other._y;
        }

        public int distanceTo(Coord coord) {
            return Math.abs(this._x - coord._x) + Math.abs(_y - coord._y);
        }
    }

    public static void run() throws IOException {
        List<Coord> targetCoordinates = Files.readAllLines(Paths.get("inputs/input6.txt")) //
                .stream() //
                .map(Coord::parse) //
                .collect(Collectors.toList());

        // Part 1
        // Run twice with different grid sizes to find largest area size that does not
        // changed between runs.
        List<Integer> areaSizes = areaSizes(GRID_SIZE, targetCoordinates);
        List<Integer> areaSizes2 = areaSizes(GRID_SIZE + 100, targetCoordinates);

        areaSizes.retainAll(areaSizes2);
        Integer maxAreaSize = areaSizes.stream().max(Integer::compareTo).get();

        // Part 2
        List<Coord> coordinates = generateCoordinates(GRID_SIZE);
        long coordInRegion = coordinates //
                .stream() //
                .filter(c -> sumOfDistances(c, targetCoordinates) < MAX_SUM_OF_DIST) //
                .count();

        // Results
        LOGGER.info("Part 1 : Max area size: " + maxAreaSize);
        LOGGER.info("Part 2 : Coordinates in region: " + coordInRegion);
    }

    private static List<Integer> areaSizes(int gridSize, List<Coord> targetCoordinates) {
        Map<Coord, Integer> coordinatesPerTargetCoord = new HashMap<>();
        List<Coord> coordinates = generateCoordinates(gridSize);

        coordinates.forEach(c -> {
            Optional<Coord> closestTgtCoord = closestTo(c, targetCoordinates);
            closestTgtCoord.ifPresent(tgt -> coordinatesPerTargetCoord.merge(tgt, 1, Integer::sum));
        });
        return coordinatesPerTargetCoord.values().stream().sorted().collect(Collectors.toList());
    }

    private static List<Coord> generateCoordinates(int gridSize) {
        List<Coord> result = new ArrayList<>();
        for (int x = -gridSize; x < gridSize; x++) {
            for (int y = -gridSize; y < gridSize; y++) {
                result.add(new Coord(x, y));
            }
        }
        return result;
    }

    private static Optional<Coord> closestTo(Coord coord, List<Coord> coordinates) {
        // Find the minimum distance
        int minDist = coordinates.stream().mapToInt(c -> c.distanceTo(coord)).min().getAsInt();

        // Find how many coordinates that have this distance
        List<Coord> collect = coordinates.stream().filter(c -> c.distanceTo(coord) == minDist)
                .collect(Collectors.toList());

        // If two (or more) coordinates have the same distance they don't count.
        return collect.size() == 1 ? Optional.of(collect.get(0)) : Optional.empty();
    }

    private static Integer sumOfDistances(Coord coord, List<Coord> coordinates) {
        return coordinates.stream().map(c -> c.distanceTo(coord)).reduce(0, Integer::sum);
    }

}