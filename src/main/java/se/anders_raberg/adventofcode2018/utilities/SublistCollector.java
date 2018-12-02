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

public class SublistCollector<T> implements Collector<T, List<List<T>>, List<List<T>>> {
    private final int _size;
    private final boolean _includeIncomplete;
    
    private List<T> _tmpList = new ArrayList<>();

    public SublistCollector(int size, boolean includeIncomplete) {
        _size = size;
        _includeIncomplete = includeIncomplete;
    }

    @Override
    public Supplier<List<List<T>>> supplier() {
        return new Supplier<List<List<T>>>() {

            @Override
            public List<List<T>> get() {
                return new ArrayList<>();
            }
        };
    }

    @Override
    public BiConsumer<List<List<T>>, T> accumulator() {
        return new BiConsumer<List<List<T>>, T>() {

            @Override
            public void accept(List<List<T>> t, T u) {
                synchronized (_tmpList) {
                    _tmpList.add(u);
                    if (_tmpList.size() == _size) {
                        t.add(_tmpList);
                        _tmpList = new ArrayList<>();
                    }
                }
            }
        };
    }

    @Override
    public BinaryOperator<List<List<T>>> combiner() {
        return new BinaryOperator<List<List<T>>>() {

            @Override
            public List<List<T>> apply(List<List<T>> t, List<List<T>> u) {
                List<List<T>> combined = new ArrayList<>();
                combined.addAll(t);
                combined.addAll(u);
                return combined;
            }
        };
    }

    @Override
    public Function<List<List<T>>, List<List<T>>> finisher() {
        return new Function<List<List<T>>, List<List<T>>>() {

            @Override
            public List<List<T>> apply(List<List<T>> t) {
                synchronized (_tmpList) {
                    if (!_tmpList.isEmpty() && _includeIncomplete) {
                        t.add(_tmpList);
                    }
                }
                return t;
            }
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return new HashSet<>();
    }

}