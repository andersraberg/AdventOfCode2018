package se.anders_raberg.adventofcode2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import se.anders_raberg.adventofcode2018.utilities.Pair;

public class Day16 {
    private static final Logger LOGGER = Logger.getLogger(Day16.class.getName());
    private final static Pattern PATTERN = Pattern.compile("Before: \\[(.+)\\]\\n+(.+)\\nAfter:  \\[(.+)\\]");

    @FunctionalInterface
    private interface Opcode {
        List<Integer> apply(List<Integer> registers, int a, int b, int c);
    }

    private static class OpSample {
        private final List<Integer> _before;
        private final List<Integer> _after;
        private final int _no;
        private final int _a;
        private final int _b;
        private final int _c;

        public OpSample(List<Integer> before, List<Integer> operation, List<Integer> after) {
            _before = before;
            _after = after;
            _no = operation.get(0);
            _a = operation.get(1);
            _b = operation.get(2);
            _c = operation.get(3);
        }
    }

    public static void run() throws IOException {
        String rawData = new String(Files.readAllBytes(Paths.get("inputs/input16a.txt"))).trim();
        List<OpSample> opSamples = new ArrayList<>();

        Matcher m = PATTERN.matcher(rawData);
        while (m.find()) {
            opSamples.add(new OpSample(parseList(m.group(1)), parseList(m.group(2)), parseList(m.group(3))));
        }

        Map<String, Opcode> opcodes = generateOpcodes();

        long numberOfSamples = opSamples.stream()
                .map(o -> opcodes.entrySet().stream()
                        .filter(e -> e.getValue().apply(o._before, o._a, o._b, o._c).equals(o._after))
                        .map(e -> e.getKey()).count())
                .filter(c -> c >= 3).count();

        LOGGER.info("Part 1: Number of samples: " + numberOfSamples);

        Map<Integer, String> opcodeMapping = new HashMap<>();
        Pair<Integer, String> pair;
        
        for (int i = 0; i < 16; i++) {
            // Find the opcode number that that has a unique opcode name,
            // add it to the map, remove it from the opcodes and then find the
            // next.
            pair = getUniqueOpcode(opcodes, opSamples);
            opcodeMapping.put(pair.first(), pair.second());
            opcodes.remove(pair.second());
        }

        Map<String, Opcode> opcodes2 = generateOpcodes();
        List<String> lines = Files.readAllLines(Paths.get("inputs/input16b.txt"));

        List<Integer> registers = Arrays.asList(0, 0, 0, 0);
        for (String line : lines) {
            String[] split = line.trim().split(" ");
            registers = opcodes2.get(opcodeMapping.get(Integer.parseInt(split[0]))).apply(registers,
                    Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
        }

        LOGGER.info("Part 2: Register 0: " + registers.get(0));

    }

    private static Pair<Integer, String> getUniqueOpcode(Map<String, Opcode> opcodes, List<OpSample> opSamples) {
        List<Pair<Integer, List<String>>> possibleOpcodes = opSamples.stream()
                .map(o -> new Pair<>(o._no,
                        opcodes.entrySet().stream()
                                .filter(e -> e.getValue().apply(o._before, o._a, o._b, o._c).equals(o._after))
                                .map(e -> e.getKey()).collect(Collectors.toList())))
                .collect(Collectors.toList());

        Pair<Integer, List<String>> collect = possibleOpcodes.stream().filter(f -> f.second().size() == 1).findFirst()
                .get();

        return new Pair<>(collect.first(), collect.second().get(0));
    }

    private static List<Integer> parseList(String str) {
        return Arrays.stream(str.split("[, ]+")).map(Integer::parseInt).collect(Collectors.toList());
    }

    private static Map<String, Opcode> generateOpcodes() {
        Map<String, Opcode> opcodes = new HashMap<>();
        opcodes.put("addr", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, r.get(a) + r.get(b)); return w;});
        opcodes.put("addi", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, r.get(a) + b); return w;});
        opcodes.put("mulr", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, r.get(a) * r.get(b)); return w;});
        opcodes.put("muli", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, r.get(a) * b); return w;});
        opcodes.put("banr", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, r.get(a) & r.get(b)); return w;});
        opcodes.put("bani", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, r.get(a) & b); return w;});
        opcodes.put("borr", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, r.get(a) | r.get(b)); return w;});
        opcodes.put("bori", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, r.get(a) | b); return w;});
        opcodes.put("setr", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, r.get(a)); return w;});
        opcodes.put("seti", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, a); return w;});
        opcodes.put("gtir", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, a > r.get(b) ? 1 : 0); return w;});
        opcodes.put("gtri", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, r.get(a) > b ? 1 : 0); return w;});
        opcodes.put("gtrr", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, r.get(a) > r.get(b) ? 1 : 0); return w;});
        opcodes.put("eqir", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, a == r.get(b) ? 1 : 0); return w;});
        opcodes.put("eqri", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, r.get(a) == b ? 1 : 0); return w;});
        opcodes.put("eqrr", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, r.get(a) == r.get(b) ? 1 : 0); return w;});
        return opcodes;
    }

}
