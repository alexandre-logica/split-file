package com.bigid.module.aggregator;

import com.bigid.domain.Location;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Aggregator {

    private static Aggregator INSTANCE;
    private HashMap<String, List<Location>> results;

    private Aggregator(){
        synchronized (Aggregator.class){
            results = new HashMap<>();
        }
    }

    public static Aggregator getInstance(){
        synchronized (Aggregator.class){
            if(null == INSTANCE){
                INSTANCE = new Aggregator();
            }
            return INSTANCE;
        }
    }

    public void aggregateResults(HashMap<String, List<Location>> matcherResults){
        synchronized (Aggregator.class) {
            matcherResults.entrySet().stream().forEach(e -> {
                        results.merge(e.getKey(), e.getValue(), (v1, v2) -> Stream.concat(v1.stream(), v2.stream())
                                .collect(Collectors.toList()));
                    });
        }
    }

    public void printResults(){
        synchronized (Aggregator.class) {
            results.entrySet().stream().forEach(e -> System.out.println(e));
        }
    }
}
