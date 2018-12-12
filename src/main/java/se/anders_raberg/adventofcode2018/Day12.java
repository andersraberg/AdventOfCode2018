package se.anders_raberg.adventofcode2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import se.anders_raberg.adventofcode2018.utilities.Pair;

public class Day12 {
    private static final Logger LOGGER = Logger.getLogger(Day12.class.getName());
    private static final int OFFSET = 1000;
    private static final Pattern PATTERN_INITSTATE = Pattern.compile("initial state: (.*)");
    private static final String PATTERN_RULE = "(.....) => (.)";

    private static class Rule extends Pair<String, String> {

        public Rule(String first, String second) {
            super(first, second);
        }

        public static Rule parseRule(String string) {
            Matcher m = Pattern.compile(PATTERN_RULE).matcher(string);
            m.find();
            return new Rule(m.group(1), m.group(2));
        }
    }

    public static void run() throws IOException {
        List<String> changes = Files.readAllLines(Paths.get("inputs/input12.txt"));
        List<Rule> rules = changes.stream() //
                .filter(r -> r.matches(PATTERN_RULE)) //
                .map(Rule::parseRule) //
                .collect(Collectors.toList());

        Matcher m = PATTERN_INITSTATE.matcher(changes.get(0));
        m.find();
        String padding = String.join("", Collections.nCopies(OFFSET, "."));
        String initialPots = padding + m.group(1) + padding;

        // Part 1
        String potsPart1 = initialPots;
        for (int i = 0; i < 20; i++) {
            potsPart1 = doOneGeneration(potsPart1, rules);
        }

        LOGGER.info("Part 1 : Sum : " + calcSum(potsPart1));

        // Part 2
        // Searching for repetitions in the pots or the sums reveals that
        // after a few hundred generations the sum for the next generation
        // increases with the same amount (33). This reduces the problem to
        // a simple arithmetic expression.
        String potsPart2 = initialPots;
        for (int i = 0; i < 200; i++) {
            potsPart2 = doOneGeneration(potsPart2, rules);
        }
        long largeSum = calcSum(potsPart2) + (50_000_000_000l - 200) * 33;

        LOGGER.info("Part 2 : Sum :" + largeSum);

    }

    private static String doOneGeneration(String pots, List<Rule> rules) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pots.length(); i++) {
            if (i < 2 || i > pots.length() - 3) {
                sb.append(".");
            } else {
                String slice = pots.substring(i - 2, i + 3);
                Optional<Rule> rule = rules.stream() //
                        .filter(r -> r.first().equals(slice)) //
                        .findFirst(); //

                sb.append(rule.get().second());
            }
        }
        return sb.toString();
    }

    private static int calcSum(String pots) {
        String[] split = pots.split("");
        int sum = 0;
        for (int i = 0; i < split.length; i++) {
            if (split[i].equals("#")) {
                sum = sum + i - OFFSET;
            }
        }
        return sum;
    }

}