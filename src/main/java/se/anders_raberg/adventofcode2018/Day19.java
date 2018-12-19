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

public class Day19 {
    private static final Map<String, Opcode> OPCODES = new HashMap<>();
    
    static {
        OPCODES.put("addr", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, r.get(a) + r.get(b)); return w;});
        OPCODES.put("addi", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, r.get(a) + b); return w;});
        OPCODES.put("mulr", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, r.get(a) * r.get(b)); return w;});
        OPCODES.put("muli", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, r.get(a) * b); return w;});
        OPCODES.put("banr", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, r.get(a) & r.get(b)); return w;});
        OPCODES.put("bani", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, r.get(a) & b); return w;});
        OPCODES.put("borr", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, r.get(a) | r.get(b)); return w;});
        OPCODES.put("bori", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, r.get(a) | b); return w;});
        OPCODES.put("setr", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, r.get(a)); return w;});
        OPCODES.put("seti", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, a); return w;});
        OPCODES.put("gtir", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, a > r.get(b) ? 1 : 0); return w;});
        OPCODES.put("gtri", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, r.get(a) > b ? 1 : 0); return w;});
        OPCODES.put("gtrr", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, r.get(a) > r.get(b) ? 1 : 0); return w;});
        OPCODES.put("eqir", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, a ==r.get(b) ? 1 : 0); return w;});
        OPCODES.put("eqri", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, r.get(a) == b ? 1 : 0); return w;});
        OPCODES.put("eqrr", (r, a, b, c) -> {List<Integer> w = new ArrayList<>(r); w.set(c, r.get(a).equals(r.get(b)) ? 1 : 0); return w;});
    }

    private static final Logger LOGGER = Logger.getLogger(Day19.class.getName());
    private final static String PATTERN = "([a-z]+) (\\d+) (\\d+) (\\d+)";

    @FunctionalInterface
    private interface Opcode {
        List<Integer> apply(List<Integer> registers, int a, int b, int c);
    }

    private static class Operation {
        private final Opcode _opcode;
        private final int _a;
        private final int _b;
        private final int _c;

        public Operation(Opcode opcode, int a, int b, int c) {
            _opcode = opcode;
            _a = a;
            _b = b;
            _c = c;
        }

        public static Operation parseOperation(String str) {
            Matcher m = Pattern.compile(PATTERN).matcher(str);
            m.find();
            return new Operation(OPCODES.get(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)),
                    Integer.parseInt(m.group(4)));
        }
    }

    public static void run() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/input19.txt"));
        List<Operation> operations = lines.stream().filter(l -> l.matches(PATTERN))//
                .map(Operation::parseOperation) //
                .collect(Collectors.toList());

        List<Integer> registers = Arrays.asList(0, 0, 0, 0, 0, 0);
        final int ipRegister = 1;
        int ip = 0;

        while (ip < operations.size()) {
            Operation op = operations.get(ip);
            registers.set(ipRegister, ip);
            registers = op._opcode.apply(registers, op._a, op._b, op._c);
            ip = registers.get(ipRegister);
            ip++;
        }

        LOGGER.info("Part 1 : Register 0 : " + registers.get(0));
    }

}
