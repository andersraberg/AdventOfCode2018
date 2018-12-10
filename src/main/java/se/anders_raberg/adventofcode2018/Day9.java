package se.anders_raberg.adventofcode2018;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Day9 {
    private static final Logger LOGGER = Logger.getLogger(Day9.class.getName());
    private static final int PLAYERS = 431;
    private static final int LAST_MARBLE_POINTS = 70950;

    private static class Ring {
        private final List<Integer> _marbles = new ArrayList<>();
        private int _index = 0;

        public Ring() {
            // _marbles.ensureCapacity(70950*10);
        }

        public void place(int marble) {
            int size = _marbles.size();
            if (size < 2 || _index == size - 2) {
                _marbles.add(marble);
                _index = _marbles.size() + -1;
            } else {
                _index = ((_index + 2) % size + size) % size;
                _marbles.add(_index, marble);
            }
        }

        public int remove() {
            int size = _marbles.size();
            _index = ((_index - 7) % size + size) % size;
            return _marbles.remove(_index);
        }

        @Override
        public String toString() {
            return _index + " : " + _marbles;
        }
    }

    public static void run() {
        int[] players = new int[PLAYERS + 1];
        Ring ring = new Ring();
        ring.place(0);

        for (int i = 1; i <= LAST_MARBLE_POINTS; i++) {
            if (i % 23 == 0) {
                int removed = ring.remove();
                players[i % PLAYERS] = players[i % PLAYERS] + i + removed;
            } else {
                ring.place(i);
            }
        }

        int max = Arrays.stream(players).max().getAsInt();
        LOGGER.info("MAX: " + max);
    }

}
