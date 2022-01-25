package com.bigid.module.matcher;

import com.bigid.domain.Location;
import com.bigid.module.aggregator.Aggregator;
import com.bigid.util.Config;
import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Matcher implements Runnable{

    HashMap<String, List<Location>> matcherResult = new HashMap<>();
    Aggregator aggregator = Aggregator.getInstance();
    private Integer lineCount;
    private Integer charCountByTextPart;
    private Integer charCountByLine = 0;
    private String matcherName;
    private List<String> textPart;
    public Matcher(Integer lineCount, Integer charCountByTextPart, List<String> textPart, String matcherName){
        this.lineCount = lineCount;
        this.charCountByTextPart = charCountByTextPart;
        this.textPart = textPart;
        this.matcherName = matcherName;
    }

    private void setResult(Collection<Emit> emits, Integer lineOffset){
        List<Location> locations = new ArrayList<>();
        Location location;
        for(Emit emit : emits){
            location = new Location();
            location.setLineOffset(lineOffset);
            location.setCharOffset(charCountByTextPart + charCountByLine + emit.getStart());
            locations.add(location);
            matcherResult.merge(emit.getKeyword(), locations, (v1, v2) -> Stream.concat(v1.stream(), v2.stream())
                    .collect(Collectors.toList()));
        }
    }

    @Override
    public void run() {
        Trie trie = Trie.builder()
                .ignoreOverlaps()
                .ignoreCase()
                .onlyWholeWords()
                .addKeywords(Config.SEARCH_TOKEN).build();
        charCountByLine = 0;
        for(String line : textPart) {
            Collection<Emit> emits = trie.parseText(line);
            if(emits.size() > 0){
                Integer lineOffset = lineCount + textPart.indexOf(line);
                setResult(emits, lineOffset);
            }
            charCountByLine += line.length()+1;
        }
        aggregator.aggregateResults(matcherResult);
    }

    private void report(String order){
        Date d = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss.SSS");
        System.out.println(order + " Time for"
                + " task name - "+ matcherName +" = " +ft.format(d));
    }
}
