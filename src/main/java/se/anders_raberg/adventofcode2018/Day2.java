package se.anders_raberg.adventofcode2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Day2 {
    private static final Logger LOGGER = Logger.getLogger(Day2.class.getName());

    public static void run() throws IOException {
        List<String> boxIds = Files.readAllLines(Paths.get("inputs/input2.txt"));

        // Part 1
        long twoOccurances = boxIds.stream().filter(id -> hasNoccurances(2, id)).count();
        long threeOccurances = boxIds.stream().filter(id -> hasNoccurances(3, id)).count();
        long checksum = twoOccurances * threeOccurances;

        // Part 2
        List<String> idsDiffInOnePos = new ArrayList<>();
        for (int i = 0; i < boxIds.size(); i++) {
            for (int j = 0; j < boxIds.size(); j++) {
                if (diffingExactlyOnePosition(boxIds.get(i), boxIds.get(j))) {
                    idsDiffInOnePos.add(boxIds.get(j));
                }
            }
        }

        // Results
        LOGGER.info("Part 1: Checksum: " + checksum);
        LOGGER.info("Part 2: BoxIds: " + idsDiffInOnePos);
    }

    private static boolean hasNoccurances(int n, String boxId) {
        return Arrays.stream(boxId.split("")) //
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())) //
                .values() //
                .stream() //
                .filter(c -> c == n) //
                .count() > 0;
    }

    private static boolean diffingExactlyOnePosition(String s1, String s2) {
        int count = 0;
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                count++;
            }
        }
        return count == 1;
    }

}
