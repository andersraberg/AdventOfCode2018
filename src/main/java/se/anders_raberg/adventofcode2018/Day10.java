package se.anders_raberg.adventofcode2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day10 {
    private static final Logger LOGGER = Logger.getLogger(Day9.class.getName());

    private static class Point {
        private final static Pattern PATTERN = Pattern.compile("position=<(.*),(.*)> velocity=<(.*),(.*)>");
        private final long _x;
        private final long _y;
        private final long _dx;
        private final long _dy;

        public Point(long x, long y, long dx, long dy) {
            _x = x;
            _y = y;
            _dx = dx;
            _dy = dy;
        }

        public static Point parsePoint(String str) {
            Matcher m = PATTERN.matcher(str);
            m.find();
            return new Point(Long.parseLong(m.group(1).trim()), //
                    Long.parseLong(m.group(2).trim()), //
                    Long.parseLong(m.group(3).trim()), //
                    Long.parseLong(m.group(4).trim())); //
        }

        public boolean equalPos(Point other) {
            return _x == other._x && _y == other._y;
        }

    }

    public static void run() throws IOException {
        List<Point> points = Files.readAllLines(Paths.get("inputs/input10.txt")) //
                .stream() //
                .map(Point::parsePoint) //
                .collect(Collectors.toList());

        long iterations = 0;
        while (true) {
            // Do one iteration
            List<Point> pointsafter = points.stream().map(p -> new Point(p._x + p._dx, p._y + p._dy, p._dx, p._dy))
                    .collect(Collectors.toList());

            // Search for when size of the minimal containing square starts to increase
            // again.
            if (minSquareSize(pointsafter) > minSquareSize(points)) {
                printSky(points);
                LOGGER.info("Iterations: " + iterations);
                break;
            }
            iterations++;
            points = pointsafter;
        }
    }

    private static long minSquareSize(List<Point> points) {
        long xMin = points.stream().mapToLong(p -> p._x).min().getAsLong();
        long xMax = points.stream().mapToLong(p -> p._x).max().getAsLong();
        long yMin = points.stream().mapToLong(p -> p._y).min().getAsLong();
        long yMax = points.stream().mapToLong(p -> p._y).max().getAsLong();
        return (xMax - xMin) * (yMax - yMin);
    }

    private static void printSky(List<Point> points) {
        long xMin = points.stream().mapToLong(p -> p._x).min().getAsLong();
        long xMax = points.stream().mapToLong(p -> p._x).max().getAsLong();
        long yMin = points.stream().mapToLong(p -> p._y).min().getAsLong();
        long yMax = points.stream().mapToLong(p -> p._y).max().getAsLong();

        StringBuilder sb = new StringBuilder();
        for (long j = yMin; j <= yMax; j++) {
            for (long i = xMin; i <= xMax; i++) {
                Point ppp = new Point(i, j, 0, 0);
                sb.append(points.stream().anyMatch(p -> p.equalPos(ppp)) ? "#" : ".");
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());

    }

}