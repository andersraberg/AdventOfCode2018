package se.anders_raberg.adventofcode2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

public class Day18 {
    private static final int AREA_SIZE = 50;
    private static final Logger LOGGER = Logger.getLogger(Day18.class.getName());

    public static void run() throws IOException {
        String[][] lumberArea = new String[AREA_SIZE][AREA_SIZE];
        List<String> lines = Files.readAllLines(Paths.get("inputs/input18.txt"));
        for (int i = 0; i < lines.size(); i++) {
            lumberArea[i] = lines.get(i).split("");
        }

        String[][] nextLumberArea;

        for (int i = 0; i < 1000 + ((1_000_000_000 - 1000) % 28); i++) {
            nextLumberArea = deepCopy(lumberArea);
            for (int y = 0; y < AREA_SIZE; y++) {
                for (int x = 0; x < AREA_SIZE; x++) {
                    if (lumberArea[y][x].equals(".") && findNumberAdjacent("|", x, y, lumberArea) > 2) {
                        nextLumberArea[y][x] = "|";
                    }
                    if (lumberArea[y][x].equals("|") && findNumberAdjacent("#", x, y, lumberArea) > 2) {
                        nextLumberArea[y][x] = "#";
                    }
                    if (lumberArea[y][x].equals("#")) {
                        if (findNumberAdjacent("|", x, y, lumberArea) > 0
                                && findNumberAdjacent("#", x, y, lumberArea) > 0) {
                            nextLumberArea[y][x] = "#";
                        } else {
                            nextLumberArea[y][x] = ".";
                        }
                    }
                }
            }
            lumberArea = nextLumberArea;
            if (i == 9) {
                LOGGER.info("Part 1: " + (count("#", lumberArea) * count("|", lumberArea)));
            }
        }
        LOGGER.info("Part 2: " + (count("#", lumberArea) * count("|", lumberArea)));
    }

    private static String[][] deepCopy(String[][] orig) {
        String[][] copy = new String[AREA_SIZE][AREA_SIZE];
        for (int y = 0; y < AREA_SIZE; y++) {
            for (int x = 0; x < AREA_SIZE; x++) {
                copy[y][x] = new String(orig[y][x]);
            }
        }
        return copy;
    }

    private static int count(String type, String[][] lumberArea) {
        int counter = 0;
        for (int y = 0; y < AREA_SIZE; y++) {
            for (int x = 0; x < AREA_SIZE; x++) {
                if (lumberArea[y][x].equals(type)) {
                    counter++;
                }
            }
        }
        return counter;
    }

    private static int findNumberAdjacent(String str, int x, int y, String[][] lumberArea) {
        int counter = 0;

        if (inRange(x - 1) && lumberArea[y][x - 1].equals(str)) {
            counter++;
        }
        if (inRange(x - 1) && inRange(y - 1) && lumberArea[y - 1][x - 1].equals(str)) {
            counter++;
        }
        if (inRange(y - 1) && lumberArea[y - 1][x].equals(str)) {
            counter++;
        }
        if (inRange(x + 1) && inRange(y - 1) && lumberArea[y - 1][x + 1].equals(str)) {
            counter++;
        }
        if (inRange(x + 1) && lumberArea[y][x + 1].equals(str)) {
            counter++;
        }
        if (inRange(x + 1) && inRange(y + 1) && lumberArea[y + 1][x + 1].equals(str)) {
            counter++;
        }
        if (inRange(y + 1) && lumberArea[y + 1][x].equals(str)) {
            counter++;
        }
        if (inRange(x - 1) && inRange(y + 1) && lumberArea[y + 1][x - 1].equals(str)) {
            counter++;
        }
        return counter;
    }

    private static boolean inRange(int a) {
        return a >= 0 && a < AREA_SIZE;
    }

}