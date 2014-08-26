package com.hulaa.backend.agoda;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.EnumSet;
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
 * <p/>
 * http://github.com/jasoet
 * http://bitbucket.com/jasoet
 *
 * @jasoet
 */

public class JsonArrayCollector implements Collector<JsonObject, JsonArray, JsonArray> {
    private static final Set<Characteristics> characteristics = EnumSet.of(Characteristics.UNORDERED);

    @Override
    public Supplier<JsonArray> supplier() {
        return new Supplier<JsonArray>() {
            @Override
            public JsonArray get() {
                return new JsonArray();
            }
        };
    }

    @Override
    public BiConsumer<JsonArray, JsonObject> accumulator() {
        return new BiConsumer<JsonArray, JsonObject>() {
            @Override
            public void accept(JsonArray arr, JsonObject o) {
                arr.add(o);
            }
        };
    }

    @Override
    public BinaryOperator<JsonArray> combiner() {
        return new BinaryOperator<JsonArray>() {
            @Override
            public JsonArray apply(JsonArray arrOne, JsonArray arrTwo) {
                arrOne.addAll(arrTwo);
                return arrOne;
            }
        };
    }

    @Override
    public Function<JsonArray, JsonArray> finisher() {
        return new Function<JsonArray, JsonArray>() {
            @Override
            public JsonArray apply(JsonArray arr) {
                return arr;
            }
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return characteristics;
    }
}
