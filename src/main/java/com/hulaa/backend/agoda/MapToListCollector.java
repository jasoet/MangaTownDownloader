package com.hulaa.backend.agoda;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Created by Deny Prasetyo,S.T
 * Java(Script) Developer and Trainer
 * Software Engineer
 * jasoet87@gmail.com
 * <p>
 * http://github.com/jasoet
 * http://bitbucket.com/jasoet
 *
 * @jasoet
 */

public class MapToListCollector<S, T> implements Collector<Map<S, T>, Map<S, List<T>>, Map<S, List<T>>> {
    @Override
    public Supplier<Map<S, List<T>>> supplier() {
        return HashMap::new;
    }

    @Override
    public BiConsumer<Map<S, List<T>>, Map<S, T>> accumulator() {
        return (stringListMap, stringStringMap) -> {
            if (stringStringMap != null) {
                stringStringMap.forEach((s, s2) -> {
                    if (stringListMap.containsKey(s)) {
                        stringListMap.get(s).add(s2);
                    } else {
                        List<T> ls = new ArrayList<>();
                        ls.add(s2);
                        stringListMap.put(s, ls);
                    }
                });
            }
        };
    }

    @Override
    public BinaryOperator<Map<S, List<T>>> combiner() {
        return (stringListMap, stringListMap2) -> {
            stringListMap2.forEach((s, strings) -> {
                if (stringListMap.containsKey(s)) {
                    stringListMap.get(s).addAll(strings);
                } else {
                    stringListMap.put(s, strings);
                }
            });
            return stringListMap;
        };
    }

    @Override
    public Function<Map<S, List<T>>, Map<S, List<T>>> finisher() {
        return new Function<Map<S, List<T>>, Map<S, List<T>>>() {
            @Override
            public Map<S, List<T>> apply(Map<S, List<T>> sListMap) {
                return sListMap;
            }
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.UNORDERED);
    }
}
