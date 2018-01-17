package com.ftd.services.search.bl.clients;

import com.ftd.services.search.config.GlobalConstants;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;

public class BaseClient {


    public static final String CLIENT_ID_HEADER =
            GlobalConstants.getApplicationName() +
                    GlobalConstants.COLON +
                    GlobalConstants.getEnvironment();

    protected void logIdentification(Logger logger,
                                     String baseUrl,
                                     String version,
                                     Boolean enabled) {
        logger.info(GlobalConstants.LOG_IDENTIFICATION, baseUrl, version, enabled);
    }


    protected HttpHeaders createHttpHeaders(String version) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(GlobalConstants.VERSION, version);
        headers.add(GlobalConstants.CLIENT_ID, CLIENT_ID_HEADER);
        headers.add(HttpHeaders.ACCEPT, GlobalConstants.APPLICATION_JSON);
        return headers;
    }
}
