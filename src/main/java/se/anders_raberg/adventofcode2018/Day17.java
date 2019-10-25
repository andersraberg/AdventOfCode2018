package se.anders_raberg.adventofcode2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import se.anders_raberg.adventofcode2018.utilities.Pair;

public class Day17 {
    private static final Logger LOGGER = Logger.getLogger(Day17.class.getName());
    private static final Pattern PATTERN_Y_RANGE = Pattern.compile("x=(\\d+), y=(\\d+)\\.\\.(\\d+)");
    private static final Pattern PATTERN_X_RANGE = Pattern.compile("y=(\\d+), x=(\\d+)\\.\\.(\\d+)");

    private static class Coord {
        private final int _x;
        private final int _y;

        public Coord(int x, int y) {
            _x = x;
            _y = y;
        }

        @Override
        public String toString() {
            return "(" + _x + ", " + _y + ")";
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

    }

    public static void run() throws IOException {
        Set<Coord> mud = new HashSet<>();
        List<String> lines = Files.readAllLines(Paths.get("inputs/input17.txt"));

        for (String string : lines) {
            Matcher my = PATTERN_Y_RANGE.matcher(string);
            Matcher mx = PATTERN_X_RANGE.matcher(string);
            if (my.find()) {
                int x = Integer.parseInt(my.group(1));
                int yLow = Integer.parseInt(my.group(2));
                int yHigh = Integer.parseInt(my.group(3));
                IntStream.range(yLow, yHigh + 1).mapToObj(y -> new Coord(x, y)).forEach(mud::add);
            }
            if (mx.find()) {
                int y = Integer.parseInt(mx.group(1));
                int xLow = Integer.parseInt(mx.group(2));
                int xHigh = Integer.parseInt(mx.group(3));
                IntStream.range(xLow, xHigh + 1).mapToObj(x -> new Coord(x, y)).forEach(mud::add);
            }
        }

        final int minX = mud.stream().mapToInt(m -> m._x).min().getAsInt();
        final int maxX = mud.stream().mapToInt(m -> m._x).max().getAsInt();
        final int minY = mud.stream().mapToInt(m -> m._y).min().getAsInt();
        final int maxY = mud.stream().mapToInt(m -> m._y).max().getAsInt();

        String[][] scan = new String[maxY + 10][maxX + 20];

        for (int y = 0; y < scan.length; y++) {
            for (int x = 0; x < scan[0].length; x++) {
                scan[y][x] = mud.contains(new Coord(x, y)) ? "#" : ".";
            }
        }

        scan[0][500] = "+";
        while (true) {
            Coord waterBlockPos = new Coord(500, 0);

            Set<Coord> visited = new HashSet<>();
            boolean blockSet = false;
            while (true) {
                visited.add(waterBlockPos);
                int y = waterBlockPos._y;
                int x = waterBlockPos._x;
                if (y + 1 <= maxY) {
                    if (scan[y + 1][x].equals(".")) {
                        waterBlockPos = new Coord(x, y + 1);
                    } else if (!scan[y + 1][x].equals("|") && scan[y][x - 1].equals(".")
                            && !visited.contains(new Coord(x - 1, y))) {
                        waterBlockPos = new Coord(x - 1, y);
                    } else if (!scan[y + 1][x].equals("|") && scan[y][x + 1].equals(".")
                            && !visited.contains(new Coord(x + 1, y))) {
                        waterBlockPos = new Coord(x + 1, y);
                    } else {
                        if (enclosedByMud(x, y, scan, minX, maxX, maxY)) {
                            scan[y][x] = "~";
                        } else {
                            scan[y][x] = "|";
                        }
                        blockSet = y == 1;
                        break;
                    }
                } else {
                    scan[y][x] = "|";
                    blockSet = y == 1;
                    break;
                }
            }
            if (blockSet) {
                break;
            }
        }

        convertRemainingRunningWaterIfEnclosed(scan, minX, maxX, minY, maxY);

        Pair<Integer, Integer> countScan = countScan(scan, minX, maxX, minY, maxY);
        LOGGER.info("Part 1 : All water: " + (countScan.first() + countScan.second()));
        LOGGER.info("Part 2 : Still water: " + countScan.second());
    }

    private static void convertRemainingRunningWaterIfEnclosed(String[][] scan, int minX, int maxX, int minY,
            int maxY) {
        for (int y = maxY; y > minY; y--) {
            for (int x = minX - 1; x < maxX + 2; x++) {
                if (scan[y][x].equals("|") && enclosedByMud(x, y, scan, minX, maxX, maxY)) {
                    scan[y][x] = "~";
                }
            }
        }
    }

    private static boolean enclosedByMud(int x, int y, String[][] scan, int minX, int maxX, int maxY) {
        boolean foundLeftBorder = false;
        boolean foundRightBorder = false;
        int startX = x;
        while (!scan[y + 1][startX].equals(".") && startX > minX - 1) {
            if (scan[y][startX].equals("#")) {
                foundLeftBorder = true;
                break;
            }
            startX--;
        }

        startX = x;
        while (!scan[y + 1][startX].equals(".") && startX < maxX + 1) {
            if (scan[y][startX].equals("#")) {
                foundRightBorder = true;
                break;
            }
            startX++;
        }

        return foundLeftBorder && foundRightBorder;
    }

    private static Pair<Integer, Integer> countScan(String[][] scan, int minX, int maxX, int minY, int maxY) {
        int runningWater = 0;
        int stillWater = 0;
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX - 1; x <= maxX + 1; x++) {
                if (scan[y][x].equals("|")) {
                    runningWater++;
                }
                if (scan[y][x].equals("~")) {
                    stillWater++;
                }
            }
        }
        return new Pair<>(runningWater, stillWater);
    }

}
