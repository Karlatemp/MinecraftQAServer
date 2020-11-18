package io.github.karlatemp.mqaserver.utils;

import java.util.logging.Logger;

public class Log {
    private static Logger logger;

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        Log.logger = logger;
    }
}
