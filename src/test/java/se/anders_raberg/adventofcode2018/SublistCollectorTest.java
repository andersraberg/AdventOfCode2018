package se.anders_raberg.adventofcode2018;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import se.anders_raberg.adventofcode2018.utilities.SublistCollector;

public class SublistCollectorTest {

    private static final List<Integer> TESTEE  =List.of(1, 2, 3, 4, 5, 6, 7, 8);

    @Test
    public void test1() {
        List<List<Integer>> expected = List.of(List.of(1, 2), List.of(3, 4), List.of(5, 6), List.of(7, 8));
        List<List<Integer>> actual = TESTEE.stream().collect(new SublistCollector<>(2, false));

        assertEquals(expected, actual);
    }

    @Test
    public void test2() {
        List<List<Integer>> expected = List.of(List.of(1, 2, 3, 4), List.of(5, 6, 7, 8));
        List<List<Integer>>  actual = TESTEE.stream().collect(new SublistCollector<>(4, false));

        assertEquals(expected, actual);
    }

}
