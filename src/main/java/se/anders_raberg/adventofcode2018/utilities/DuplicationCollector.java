package se.anders_raberg.adventofcode2018.utilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class DuplicationCollector<T> implements Collector<T, List<T>, List<T>> {
    private final int _factor;

    public DuplicationCollector(int factor) {
        _factor = factor;
    }

    @Override
    public Supplier<List<T>> supplier() {
        return new Supplier<List<T>>() {

            @Override
            public List<T> get() {
                return new ArrayList<>();
            }
        };
    }

    @Override
    public BiConsumer<List<T>, T> accumulator() {
        return new BiConsumer<List<T>, T>() {

            @Override
            public void accept(List<T> t, T u) {
                for (int i = 0; i < _factor; i++) {
                    t.add(u);
                }
            }
        };
    }

    @Override
    public BinaryOperator<List<T>> combiner() {
        return new BinaryOperator<List<T>>() {

            @Override
            public List<T> apply(List<T> t, List<T> u) {
                List<T> combined = new ArrayList<>();
                combined.addAll(t);
                combined.addAll(u);
                return combined;
            }
        };
    }

    @Override
    public Function<List<T>, List<T>> finisher() {
        return new Function<List<T>, List<T>>() {

            @Override
            public List<T> apply(List<T> t) {
                return t;
            }
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return new HashSet<>();
    }

}