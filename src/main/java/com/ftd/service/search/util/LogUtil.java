package com.ftd.service.search.util;

import org.apache.log4j.Logger;

public class LogUtil {
    public static void logTotalTimeTaken(Logger logger, String component, long startTime) {
        long endTime = System.currentTimeMillis();
        logger.info("Total " + component + " Time " + (endTime - startTime) + " milli seconds");
    }

}
