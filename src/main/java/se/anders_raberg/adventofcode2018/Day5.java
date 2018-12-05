package se.anders_raberg.adventofcode2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Day5 {
    private static final Logger LOGGER = Logger.getLogger(Day5.class.getName());

    public static void run() throws IOException {
        String[] rawData = new String(Files.readAllBytes(Paths.get("inputs/input5.txt"))).trim().split("");

        List<String> polymer = new ArrayList<>(Arrays.asList(rawData));

        // Part 1
        int reactedLengthPart1 = react(polymer);

        // Part 2
        List<String> uniqueUnits = polymer.stream() //
                .map(String::toLowerCase) //
                .distinct() //
                .collect(Collectors.toList());

        List<Integer> reatctedLengths = new ArrayList<>();

        for (String unit : uniqueUnits) {
            List<String> polymerWithFilteredOut = polymer.stream() //
                    .filter(d -> !d.equals(unit)) //
                    .filter(d -> !d.equals(unit.toUpperCase())) //
                    .collect(Collectors.toList());

            reatctedLengths.add(react(polymerWithFilteredOut));
        }

        int minReactedLengthPart2 = reatctedLengths.stream().min(Integer::compareTo).get();

        // Results
        LOGGER.info("Part 1 : " + reactedLengthPart1);
        LOGGER.info("Part 2 : " + minReactedLengthPart2);
    }

    private static int react(List<String> input) {
        boolean removedAny;
        do {
            removedAny = false;
            int i = 0;
            while (i < input.size() - 1) {
                if (doReact(input.get(i), input.get(i + 1))) {
                    input.remove(i);
                    input.remove(i);
                    removedAny = true;
                } else {
                    i++;
                }
            }
        } while (removedAny);

        return input.size();
    }

    private static boolean doReact(String a, String b) {
        return (a.toLowerCase().equals(b) && b.toUpperCase().equals(a)
                || a.toUpperCase().equals(b) && b.toLowerCase().equals(a));
    }
}
