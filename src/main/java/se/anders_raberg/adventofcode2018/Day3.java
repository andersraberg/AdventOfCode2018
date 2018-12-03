package se.anders_raberg.adventofcode2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day3 {
    private static final int SIZE_OF_FABRIC = 1000;
    private static final Logger LOGGER = Logger.getLogger(Day3.class.getName());
    private static final Pattern PATTERN = Pattern.compile("#(.*)@(.*),(.*):(.*)x(.*)");

    private static class Claim {
        private final int _id;
        private final int _left;
        private final int _top;
        private final int _width;
        private final int _height;

        public Claim(int id, int left, int top, int width, int height) {
            _id = id;
            _left = left;
            _top = top;
            _width = width;
            _height = height;
        }

        public static Claim create(String s) {
            Matcher m = PATTERN.matcher(s);
            m.find();
            // Add 1 to left/top to start on the first square of the claim
            return new Claim(match(m, 1), match(m, 2) + 1, match(m, 3) + 1, match(m, 4), match(m, 5));
        }

        private static int match(Matcher m, int pos) {
            return Integer.valueOf(m.group(pos).trim());
        }
    }

    public static void run() throws IOException {
        List<Claim> claims = Files.readAllLines(Paths.get("inputs/input3.txt")) //
                .stream() //
                .map(Claim::create) //
                .collect(Collectors.toList());

        // Fabric initialized with 0 claims per square
        int[][] fabric = new int[SIZE_OF_FABRIC][SIZE_OF_FABRIC];
        for (Claim c : claims) {
            for (int i = c._left; i < c._left + c._width; i++) {
                for (int j = c._top; j < c._top + c._height; j++) {
                    // Increase the number of claims for this square
                    fabric[i][j]++;
                }
            }
        }

        // Part 1
        int squaresOverlappingClaims = 0;
        for (int i = 0; i < SIZE_OF_FABRIC; i++) {
            for (int j = 0; j < SIZE_OF_FABRIC; j++) {
                if (fabric[i][j] > 1) {
                    squaresOverlappingClaims++;
                }
            }
        }

        // Part 2
        List<Integer> claimIdsNotOverlapping = new ArrayList<>();
        for (Claim c : claims) {
            boolean overlapping = false;
            for (int i = c._left; i < c._left + c._width; i++) {
                for (int j = c._top; j < c._top + c._height; j++) {
                    if (fabric[i][j] > 1) {
                        overlapping = true;
                    }
                }
            }

            if (!overlapping) {
                claimIdsNotOverlapping.add(c._id);
            }
        }

        // Results
        LOGGER.info("Part 1 : Overlapping squares : " + squaresOverlappingClaims);
        LOGGER.info("Part 2 : Claims not overlapping : " + claimIdsNotOverlapping);
    }

}