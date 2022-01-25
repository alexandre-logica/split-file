package com.bigid;

import com.bigid.module.aggregator.Aggregator;
import com.bigid.module.main.Main;

public class BigIdApplication {

    public static void main(String[] args) {
        Aggregator aggregator = Aggregator.getInstance();
        Main main = new Main();
        main.readText();
        aggregator.printResults();
    }
}
