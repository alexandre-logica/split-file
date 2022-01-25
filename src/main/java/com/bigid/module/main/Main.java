package com.bigid.module.main;

import com.bigid.module.aggregator.Aggregator;
import com.bigid.module.matcher.Matcher;
import com.bigid.util.Config;

import java.io.*;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    private ExecutorService pool;

    public Main(){
    }

    public void readText(){
        try {
            InputStream inputStream = new FileInputStream(Config.PATH_FILE);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            Integer lineCount = 1;
            Integer charCount = 0;
            int index = 0;
            int limit = Config.LINES_AMOUNT;
            while (bufferedReader.ready()) {
                List<String> textPart = IntStream.range(index, limit)
                        .mapToObj(i -> readLine(bufferedReader))
                        .filter(e -> e != null)
                        .collect(Collectors.toList());

                buildMatcherThread(lineCount, charCount, textPart);

                charCount += textPart.stream().mapToInt(s -> s.length()+1).sum();
                lineCount += limit;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkThreadStatus() {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for (Thread t : threadSet) {
            System.out.println("Thread :" + t + ":"
                    + "Thread status : "
                    + t.getState());
        }
    }

    private void buildMatcherThread(Integer lineCount, Integer charCount, List<String> textPart) {
        Runnable matcher = new Matcher(lineCount, charCount, textPart, "Matcher-"+lineCount.toString());
        Thread thread = new Thread(matcher);
        thread.setName("Matcher-"+lineCount.toString());
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void buildMatcherThreadPool(Integer lineCount, Integer charCount, List<String> textPart) {
        Runnable matcher = new Matcher(lineCount, charCount, textPart, "Matcher-"+lineCount.toString());
        pool.execute(matcher);
    }

    private void buildMatcher(Integer lineCount, Integer charCount, List<String> textPart) {
        Matcher matcher = new Matcher(lineCount, charCount, textPart, "Matcher-"+lineCount.toString());
        matcher.run();
    }

    private String readLine(BufferedReader reader) {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
