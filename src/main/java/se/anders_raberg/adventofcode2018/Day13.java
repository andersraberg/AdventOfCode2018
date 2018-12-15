package se.anders_raberg.adventofcode2018;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import se.anders_raberg.adventofcode2018.utilities.Pair;

public class Day13 {
    private static final int BOARD_SIZE = 150;
    private static final Logger LOGGER = Logger.getLogger(Day13.class.getName());

    private enum Direction {
        UP(0, -1), DOWN(0, 1), RIGHT(1, 0), LEFT(-1, 0);

        private final int _xStep;
        private final int _yStep;

        private Direction(int xStep, int yStep) {
            _xStep = xStep;
            _yStep = yStep;
        }

        public Direction turn(Turn turn) {
            switch (turn) {
            case LEFT:
                return turnLeft();
            case RIGHT:
                return turnRight();
            case STRAIGHT:
                return this;
            }
            return null;
        }

        public Direction turnLeft() {
            switch (this) {
            case DOWN:
                return RIGHT;
            case RIGHT:
                return UP;
            case UP:
                return LEFT;
            case LEFT:
                return DOWN;
            }
            return null;
        }

        public Direction turnRight() {
            switch (this) {
            case DOWN:
                return LEFT;
            case LEFT:
                return UP;
            case UP:
                return RIGHT;
            case RIGHT:
                return DOWN;
            }
            return null;
        }

    }

    private enum Turn {
        LEFT, STRAIGHT, RIGHT;

        public Turn next() {
            switch (this) {
            case LEFT:
                return Turn.STRAIGHT;
            case STRAIGHT:
                return Turn.RIGHT;
            case RIGHT:
                return Turn.LEFT;
            }
            return null;
        }
    }

    private static class Cart {
        private final int _x;
        private final int _y;
        private final Direction _direction;
        private final Turn _next;

        public Cart(int x, int y, Direction direction, Turn next) {
            _x = x;
            _y = y;
            _direction = direction;
            _next = next;
        }

        @Override
        public String toString() {
            return "Cart [" + _x + ", " + _y + "] " + _direction + " : " + _next;
        }

    }

    public static void run() throws IOException {
        String[][] board = new String[BOARD_SIZE][BOARD_SIZE];
        List<String> lines = Files.readAllLines(Paths.get("inputs/input13.txt"));
        for (int i = 0; i < lines.size(); i++) {
            board[i] = lines.get(i).split("");
        }

        List<Cart> carts = new ArrayList<>();
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board.length; x++) {
                if (board[y][x].matches(">")) {
                    carts.add(new Cart(x, y, Direction.RIGHT, Turn.LEFT));
                    board[y][x] = "-";
                } else if (board[y][x].matches("<")) {
                    carts.add(new Cart(x, y, Direction.LEFT, Turn.LEFT));
                    board[y][x] = "-";
                } else if (board[y][x].matches("\\^")) {
                    carts.add(new Cart(x, y, Direction.UP, Turn.LEFT));
                    board[y][x] = "|";
                } else if (board[y][x].matches("v")) {
                    carts.add(new Cart(x, y, Direction.DOWN, Turn.LEFT));
                    board[y][x] = "|";
                }
            }
        }

        Pair<Integer, Integer> firstCrashLocation = null;
        Cart lastCart;
        while (true) {
            Collections.sort(carts, Day13::compare);
            carts = carts.stream().map(c -> moveCart(c, board)).collect(Collectors.toList());

            Optional<Pair<Integer, Integer>> possibleCrashLocation = carts.stream() //
                    .map(c -> new Pair<>(c._x, c._y))
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream()
                    .filter(e -> e.getValue() > 1) //
                    .map(e -> e.getKey()) //
                    .findFirst();

            if (possibleCrashLocation.isPresent()) {
                if (firstCrashLocation == null) {
                    firstCrashLocation = possibleCrashLocation.get();
                }

                carts = carts.stream().filter(c -> !new Pair<>(c._x, c._y).equals(possibleCrashLocation.get()))
                        .collect(Collectors.toList());

                if (carts.size() == 1) {
                    lastCart = carts.get(0);
                    break;
                }
            }
        }

        LOGGER.info("Part 1 : First crash location : " + firstCrashLocation);
        LOGGER.info("Part 2 : Last cart : " + lastCart);

    }

    private static int compare(Cart cart1, Cart cart2) {
        if (cart1._x < cart2._x) {
            return -1;
        }
        return cart1._y - cart2._y;
    }

    private static Cart moveCart(Cart cart, String[][] board) {
        int x = cart._x;
        int y = cart._y;
        Direction dir = cart._direction;
        Turn next = cart._next;

        return turnCart(new Cart(x + dir._xStep, y + dir._yStep, dir, next), board);
    }

    private static Cart turnCart(Cart cart, String[][] board) {
        int x = cart._x;
        int y = cart._y;
        Direction dir = cart._direction;
        Turn next = cart._next;

        if (board[y][x].equals("/")) {
            switch (dir) {
            case DOWN:
            case UP:
                return new Cart(x, y, dir.turnRight(), next);
            case LEFT:
            case RIGHT:
                return new Cart(x, y, dir.turnLeft(), next);
            }
        } else if (board[y][x].equals("\\")) {
            switch (dir) {
            case DOWN:
            case UP:
                return new Cart(x, y, dir.turnLeft(), next);
            case LEFT:
            case RIGHT:
                return new Cart(x, y, dir.turnRight(), next);
            }
        } else if (board[y][x].equals("+")) {
            return new Cart(x, y, dir.turn(next), next.next());
        } else {
            return new Cart(x, y, dir, next);
        }
        return null;

    }

}