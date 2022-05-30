package com.bigid.module.main;

import com.bigid.module.matcher.Matcher;
import com.bigid.util.Config;
import org.ahocorasick.trie.Trie;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    private ExecutorService pool = null;

    public Main(){
    }

    public void readText() {
        try {
            InputStream inputStream = new FileInputStream(Config.PATH_FILE);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            AtomicInteger lineCount = new AtomicInteger(1);
            AtomicInteger charCount = new AtomicInteger(0);
            int index = 0;
            int limit = Config.LINES_AMOUNT;
            Trie trie = Trie.builder()
                    .ignoreOverlaps()
                    .ignoreCase()
                    .onlyWholeWords()
                    .addKeywords(Config.SEARCH_TOKEN).build();
            pool = Executors.newCachedThreadPool();
            while (bufferedReader.ready()) {
                List<String> textPart = IntStream.range(index, limit)
                        .mapToObj(i -> readLine(bufferedReader))
                        .filter(e -> e != null)
                        .collect(Collectors.toList());
                Runnable matcher = new Matcher(lineCount.get(), charCount.get(), textPart, trie);
                pool.execute(matcher);

                charCount.addAndGet(textPart.stream().mapToInt(s -> s.length() + 1).sum());
                lineCount.addAndGet(limit);

                List<int> = new ArrayList<>()
            }
            pool.awaitTermination(200, TimeUnit.MILLISECONDS);
            pool.shutdown();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e){
            e.printStackTrace();
        } finally {
            if(pool != null){
                pool.shutdownNow();
            }
        }
    }
    private String readLine(BufferedReader reader) {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
