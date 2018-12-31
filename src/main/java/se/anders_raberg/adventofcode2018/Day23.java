package se.anders_raberg.adventofcode2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day23 {

    private static class Nanobot {
        private final long _x;
        private final long _y;
        private final long _z;
        private final long _d;

        public Nanobot(long x, long y, long z, long d) {
            _x = x;
            _y = y;
            _z = z;
            _d = d;
        }

        public static Nanobot parse(String str) {
            Matcher m = PATTERN.matcher(str);
            m.find();
            return new Nanobot(Long.parseLong(m.group(1)), //
                    Long.parseLong(m.group(2)), //
                    Long.parseLong(m.group(3)), //
                    Long.parseLong(m.group(4)));
        }

        public long distanceTo(Nanobot b) {
            return Math.abs((b._x - _x)) + Math.abs((b._y - _y)) + Math.abs((b._z - _z));
        }

        @Override
        public String toString() {
            return String.format("pos=<%d,%d,%d>, r=%d", _x, _y, _z, _d);
        }
    }

    private static final Logger LOGGER = Logger.getLogger(Day23.class.getName());
    private final static Pattern PATTERN = Pattern.compile("pos=<(.+),(.+),(.+)>, r=(.+)");

    public static void run() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/input23.txt"));

        List<Nanobot> allBots = lines.stream().map(Nanobot::parse).collect(Collectors.toList());
        Nanobot nanobot = allBots.stream().sorted(Day23::compareRange).findFirst().get();
        long count = numberInRange(nanobot, allBots);

        LOGGER.info("Part 1: Nanobots in range of bot with greatest radius: " + count);
    }

    private static int compareRange(Nanobot b1, Nanobot b2) {
        return (int) (b2._d - b1._d);
    }

    private static long numberInRange(Nanobot b, List<Nanobot> allBots) {
        return allBots.stream().filter(a -> a.distanceTo(b) <= b._d).count();
    }

}
