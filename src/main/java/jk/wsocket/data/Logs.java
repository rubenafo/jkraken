package jk.wsocket.data;

import jk.app.JKrakenApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Logs {

    private static Logger LOGGER = LoggerFactory.getLogger(JKrakenApp.class);

    private Logs () {
        // hidden
    }

    public static void info (String msg, Object ...args) {
        LOGGER.info(msg, args);
    }

    public static void debug (String msg, Object ...args) {
        LOGGER.info(msg, args);
    }

}
