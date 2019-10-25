package se.anders_raberg.adventofcode2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Day7 {
    private static final Logger LOGGER = Logger.getLogger(Day7.class.getName());
    private static final int TIME_OFFSET = 60;
    private static final int NOF_WORKERS = 5;

    private static class Worker {
        private final String _step;
        private int _secondsLeft;

        public Worker(String step) {
            _step = step;
            _secondsLeft = Character.valueOf(step.charAt(0)) - Character.valueOf('A') + 1 + TIME_OFFSET;
        }

        public int stepTime() {
            return --_secondsLeft;
        }

    }

    public static void run() throws IOException {

        Map<String, Set<String>> startDependencies = new HashMap<>();

        Files.readAllLines(Paths.get("inputs/input7.txt")).forEach(l -> {
            String[] split = l.trim().split(" ");
            startDependencies.computeIfAbsent(split[1], it -> new HashSet<>());
            Set<String> set = startDependencies.computeIfAbsent(split[7], it -> new HashSet<>());
            set.add(split[1]);
        });

        // Part 1
        StringBuilder result1 = new StringBuilder();
        Map<String, Set<String>> dependencies = startDependencies;
        do {
            String nextStep = getNextStep(dependencies).get();
            result1.append(nextStep);
            dependencies = removeStepFromDeps(nextStep, removeCurrentStep(nextStep, dependencies));

        } while (!dependencies.isEmpty());

        // Part 2
        dependencies = startDependencies;
        StringBuilder result2 = new StringBuilder();
        Worker[] workers = new Worker[NOF_WORKERS];
        int time = 0;
        do {
            for (int i = 0; i < workers.length; i++) {
                if (workers[i] == null) {
                    Optional<String> nextStep = getNextStep(dependencies);
                    if (nextStep.isPresent()) {
                        workers[i] = new Worker(nextStep.get());
                        dependencies = removeCurrentStep(nextStep.get(), dependencies);
                    }
                }
            }

            for (int i = 0; i < workers.length; i++) {
                if (workers[i] != null && workers[i].stepTime() == 0) {
                    result2.append(workers[i]._step);
                    dependencies = removeStepFromDeps(workers[i]._step, dependencies);
                    workers[i] = null;
                }
            }

            time++;
        } while (!dependencies.isEmpty() || Arrays.stream(workers).anyMatch(Objects::nonNull));

        LOGGER.info("Part 1: " + result1);
        LOGGER.info("Part 2: " + result2 + " with time " + time);
    }

    private static Map<String, Set<String>> removeCurrentStep(String step, Map<String, Set<String>> dependencies) {
        return dependencies.entrySet() //
                .stream() //
                .filter(e -> !e.getKey().equals(step)) //
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    private static Map<String, Set<String>> removeStepFromDeps(String step, Map<String, Set<String>> dependencies) {
        return dependencies.entrySet() //
                .stream() //
                .collect(Collectors.toMap(Entry::getKey, e -> e.getValue() //
                        .stream() //
                        .filter(v -> !v.equals(step)).collect(Collectors.toSet())));
    }

    private static Optional<String> getNextStep(Map<String, Set<String>> dependencies) {
        return dependencies.entrySet() //
                .stream() //
                .filter(e -> e.getValue().isEmpty()) //
                .map(Entry::getKey) //
                .sorted() //
                .findFirst();
    }

}