package se.anders_raberg.adventofcode2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day4 {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final Pattern PATTERN_TIME = Pattern.compile("\\[(.*)\\](.*)");
    private static final Pattern PATTERN_BEGIN = Pattern.compile("Guard #(.*) begins shift");
    private static final Pattern PATTERN_SLEEPS = Pattern.compile("falls asleep");
    private static final Pattern PATTERN_WAKES = Pattern.compile("wakes up");
    private static final Logger LOGGER = Logger.getLogger(Day4.class.getName());

    private enum EventType {
        BEGINS, SLEEPS, WAKES;
    }

    private static class Event {
        private final LocalDateTime _date;
        private final EventType _type;
        private final int _guard;

        public Event(LocalDateTime date, EventType type, int guard) {
            _date = date;
            _type = type;
            _guard = guard;
        }

        public static Event parseEvent(String str) {
            Matcher m = PATTERN_TIME.matcher(str);
            m.find();
            LocalDateTime date;
            date = LocalDateTime.parse(m.group(1), DATE_FORMATTER);
            Matcher b = PATTERN_BEGIN.matcher(m.group(2));
            Matcher s = PATTERN_SLEEPS.matcher(m.group(2));
            Matcher w = PATTERN_WAKES.matcher(m.group(2));
            EventType type;
            int guard;
            if (b.find()) {
                type = EventType.BEGINS;
                guard = Integer.valueOf(b.group(1));
            } else if (s.find()) {
                type = EventType.SLEEPS;
                guard = -1;
            } else if (w.find()) {
                type = EventType.WAKES;
                guard = -1;
            } else {
                throw new IllegalArgumentException();
            }

            return new Event(date, type, guard);
        }

        public int compareTo(Event other) {
            return _date.compareTo(other._date);
        }

    }

    public static void run() throws IOException {
        List<Event> guardEvents = Files.readAllLines(Paths.get("inputs/input4.txt")) //
                .stream() //
                .map(Event::parseEvent) //
                .sorted(Event::compareTo) //
                .collect(Collectors.toList());

        Map<Integer, Integer> sleptMinutesPerGuard = new HashMap<>();
        Map<Integer, Map<Integer, Integer>> timesSleptPerMinPerGuard = new HashMap<>();
        Map<Integer, Map<Integer, Integer>> timesSleptPerGuardPerMin = new HashMap<>();

        // This code assumes that the events for each guard will be in the order
        // BEGIN, SLEEP, WAKE, SLEEP, WAKE, ......
        int currentGuard = 0;
        LocalDateTime sleepTime = LocalDateTime.now();
        for (Event event : guardEvents) {
            if (EventType.BEGINS.equals(event._type)) {
                currentGuard = event._guard;
            } else if (EventType.SLEEPS.equals(event._type)) {
                sleepTime = event._date;
            } else if (EventType.WAKES.equals(event._type)) {
                Integer sleptMinutes = (int) Duration.between(sleepTime, event._date).toMinutes();
                sleptMinutesPerGuard.merge(currentGuard, sleptMinutes, Integer::sum);

                Map<Integer, Integer> timesSleptPerMinForCurrentGuard = timesSleptPerMinPerGuard
                        .computeIfAbsent(currentGuard, k -> new HashMap<>());

                for (int i = sleepTime.getMinute(); i < event._date.getMinute(); i++) {
                    timesSleptPerMinForCurrentGuard.compute(i, (k, v) -> (v == null) ? 1 : v + 1);
                    Map<Integer, Integer> timesSleptPerGuardForCurrentMin = timesSleptPerGuardPerMin.computeIfAbsent(i,
                            k -> new HashMap<>());

                    timesSleptPerGuardForCurrentMin.compute(currentGuard, (k, v) -> (v == null) ? 1 : v + 1);
                }
            }
        }

        // Find guard who slept most
        Integer guard = sleptMinutesPerGuard.entrySet().stream().sorted(Day4::reverseCompare).findFirst().get()
                .getKey();

        // Find the minute he slept most often
        Map<Integer, Integer> timesSleptPerMinForThisGuard = timesSleptPerMinPerGuard.get(guard);
        Integer minute = timesSleptPerMinForThisGuard.entrySet().stream().sorted(Day4::reverseCompare).findFirst().get()
                .getKey();

        // Result
        LOGGER.info("Part 1: " + guard * minute);

        outerloop: for (int i = 59; i > 0; i--) {
            for (Entry<Integer, Map<Integer, Integer>> minuteEntry : timesSleptPerGuardPerMin.entrySet()) {
                for (Entry<Integer, Integer> guardEntry : minuteEntry.getValue().entrySet()) {
                    if (guardEntry.getValue() == i) {

                        // Result
                        LOGGER.info("Part 2: " + minuteEntry.getKey() * guardEntry.getKey());
                        break outerloop;
                    }
                }
            }
        }

    }

    private static int reverseCompare(Entry<Integer, Integer> o1, Entry<Integer, Integer> o2) {
        return (o2.getValue() - o1.getValue());
    }

}