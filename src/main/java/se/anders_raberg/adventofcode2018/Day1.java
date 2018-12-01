package se.anders_raberg.adventofcode2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Day1 {
    private static final Logger LOGGER = Logger.getLogger(Day1.class.getName());

    public static void run() throws IOException {
        List<Integer> changes = Files.readAllLines(Paths.get("inputs/input1.txt")).stream() //
                .map(Integer::valueOf) //
                .collect(Collectors.toList());

        // Part 1
        Integer resultingFrequency = changes.stream().reduce(0, Integer::sum);

        // Part 2
        int currenFreq = 0;
        Set<Integer> frequenciesReached = new HashSet<>();
        Optional<Integer> firstReachedTwice = Optional.empty();
        frequenciesReached.add(0);
        while (!firstReachedTwice.isPresent()) {
            for (Integer change : changes) {
                currenFreq = currenFreq + change;
                boolean alreadyReached = !frequenciesReached.add(currenFreq);
                if (alreadyReached) {
                    firstReachedTwice = Optional.of(currenFreq);
                    break;
                }
            }
        }

        // Results
        LOGGER.info("Part 1: Resulting frequency: " + resultingFrequency);
        LOGGER.info("Part 2: First frequency reached twice: " + firstReachedTwice.get());
    }
}
