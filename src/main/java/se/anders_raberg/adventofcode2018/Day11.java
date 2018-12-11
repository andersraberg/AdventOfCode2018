package se.anders_raberg.adventofcode2018;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import se.anders_raberg.adventofcode2018.utilities.Pair;

public class Day11 {
    private static final int GRID_SIZE = 300;
    private static final Logger LOGGER = Logger.getLogger(Day11.class.getName());
    private static final int SERIAL_NUMBER = 6042;

    public static void run() {
        int[][] grid = new int[GRID_SIZE + 1][GRID_SIZE + 1];

        for (int x = 1; x <= GRID_SIZE; x++) {
            for (int y = 1; y <= GRID_SIZE; y++) {
                grid[x][y] = calcPowerLevel(x, y, SERIAL_NUMBER);
            }
        }

        // Part 1
        Pair<Integer, Pair<Integer, Integer>> maxPart1 = calcMaxPowerForSquare(3, grid);

        // Part 2
        Pair<Integer, Pair<Integer, Pair<Integer, Integer>>> maxPart2 = IntStream.range(1, GRID_SIZE) //
                .parallel() //
                .mapToObj(i -> new Pair<>(i, calcMaxPowerForSquare(i, grid))) //
                .max(Comparator.comparingInt(a -> a.second().first())).get();

        LOGGER.info("Part 1 : Power: " + maxPart1.first() + " for " + maxPart1.second());
        LOGGER.info("Part 2 : Max power for " + maxPart2.second() + " with size " + maxPart2.first());
    }

    private static Pair<Integer, Pair<Integer, Integer>> calcMaxPowerForSquare(int squareSize, int[][] grid) {
        Map<Integer, Pair<Integer, Integer>> powerPerSquare = new HashMap<>();
        for (int x = 1; x <= GRID_SIZE - squareSize; x++) {
            for (int y = 1; y <= GRID_SIZE - squareSize; y++) {
                int powerSubMatrix = calcTotalPowerSquare(x, y, squareSize, grid);
                powerPerSquare.put(powerSubMatrix, new Pair<>(x, y));
            }
        }
        int asInteger = powerPerSquare.keySet().stream().mapToInt(Integer::intValue).max().getAsInt();
        return new Pair<>(asInteger, powerPerSquare.get(asInteger));

    }

    private static Integer calcPowerLevel(int x, int y, int serialNumber) {
        int rackId = x + 10;
        int powerLevel = rackId * y + serialNumber;
        powerLevel = powerLevel * rackId;
        String tmp = Integer.toString(powerLevel);
        powerLevel = Integer.parseInt(tmp.substring(tmp.length() - 3, tmp.length() - 2));
        return powerLevel - 5;
    }

    private static Integer calcTotalPowerSquare(int x, int y, int squareSize, int[][] grid) {
        int power = 0;
        for (int dx = x; dx < x + squareSize; dx++) {
            for (int dy = y; dy < y + squareSize; dy++) {
                power = power + grid[dx][dy];
            }
        }
        return power;
    }

}