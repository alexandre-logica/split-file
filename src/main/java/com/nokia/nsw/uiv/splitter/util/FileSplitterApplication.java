package com.nokia.nsw.uiv.splitter.util;

import com.nokia.nsw.uiv.splitter.module.processdata.FileSplitter;

/**
 * @author alesilva on 13/04/2023
 */
public class FileSplitterApplication {

    public static void main(String[] args) {
        FileSplitter fileSplitter = new FileSplitter();
        fileSplitter.readText(args);
    }
}
