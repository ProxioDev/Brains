package net.limework.brains;

import io.prometheus.client.hotspot.DefaultExports;

public class Main {

    static {
        // init the included exporters by dudes of promethium
        DefaultExports.initialize();
    }

    public static void main(String[] args) throws Exception {
        new Brains().start();
    }

}
