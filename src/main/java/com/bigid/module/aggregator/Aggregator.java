package com.bigid.module.aggregator;

import com.bigid.domain.Location;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Aggregator {

    private static Aggregator INSTANCE;
    private Map<String, List<Location>> results;

    private Aggregator(){
        results = new ConcurrentHashMap<>();
    }

    public static Aggregator getInstance(){
        if(null == INSTANCE){
            INSTANCE = new Aggregator();
        }
        return INSTANCE;
    }

    public void aggregateResults(Map<String, List<Location>> matcherResults){
        matcherResults.entrySet().stream().forEach(e -> {
                    results.merge(e.getKey(), e.getValue(), (v1, v2) -> Stream.concat(v1.stream(), v2.stream())
                            .collect(Collectors.toList()));
                });
    }

    public void printResults(){
        results.entrySet().stream().forEach(e -> System.out.println(e));
    }
}
