package com.ftd.services.search.bl.clients;

import com.ftd.services.search.api.response.Document;
import com.ftd.services.search.api.response.SearchServiceResponse;
import com.ftd.services.search.config.GlobalConstants;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    protected Set<String> getPids(SearchServiceResponse searchServiceResponse) {
        if (searchServiceResponse != null
                && searchServiceResponse.getDocumentList() != null) {

            return
                    searchServiceResponse
                            .getDocumentList()
                            .stream()
                            .map(Document::getRecord)
                            .map(e -> e.get(GlobalConstants.PID))
                            .map(String::valueOf)
                            .collect(Collectors.toSet());
        }
        return new HashSet<>();

    }

}
