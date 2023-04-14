package com.nokia.nsw.uiv.splitter.module.processdata;

import com.nokia.nsw.uiv.splitter.util.Config;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author alesilva on 12/04/2023
 */
public class FileSplitter {

    public FileSplitter(){
    }

    public void readText(String[] args) {
        int limit;
        String filePath;
        Arrays.stream(args).forEach(e -> System.out.println(e));
        System.out.println(Charset.defaultCharset());
        if(args != null && args.length == 2){
            filePath = args[0];
            limit = Integer.parseInt(args[1]);
        }else{
            limit = Config.LINES_AMOUNT;
            filePath = Config.FILE_PATH;
        }
        File[] files = new File(filePath).listFiles(filefilter);

        for (File file : Objects.requireNonNull(files)) {
            String inputFileName = file.getName().replaceFirst("[.][^.]+$", "");
            try {
                InputStream inputStream = new FileInputStream(file.getAbsolutePath());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                int index = 0;
                int count = 1;
                String header = readLine(bufferedReader);

                while (bufferedReader.ready()) {
                    List<String> textPart = IntStream.range(index, limit)
                            .mapToObj(i -> readLine(bufferedReader))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    String outputFileName = filePath + File.separator + inputFileName + count + ".csv";
                    System.out.println(outputFileName);
                    BufferedWriter br = new BufferedWriter(new FileWriter(outputFileName, Charset.defaultCharset()));
                    br.write(header + System.lineSeparator());

                    for (String line : textPart) {
                        br.write(line + System.lineSeparator());
                    }
                    br.close();
                    count++;
                }
            } catch (IOException e) {
                e.printStackTrace();
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
    private final FileFilter filefilter = file -> file.getName().endsWith("." + Config.FILE_TYPE) && !file.getName().startsWith("~");

}
