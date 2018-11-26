package se.anders_raberg.adventofcode2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Day1a {
    private static final Logger LOGGER = Logger.getLogger(Day1a.class.getName());

    public static void run() throws IOException {
        String rawData = new String(Files.readAllBytes(Paths.get("inputs/input1.txt"))).trim();

        LOGGER.log(Level.INFO, rawData);
    }
}
