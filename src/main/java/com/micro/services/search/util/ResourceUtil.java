package com.micro.services.search.util;

import com.micro.services.search.api.request.ServiceRequest;
import com.micro.services.search.api.response.From;
import com.micro.services.search.config.GlobalConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.*;

import static com.micro.services.search.config.GlobalConstants.*;

public class ResourceUtil {
    private static Logger logger = Logger.getLogger(ResourceUtil.class.getName());

    public static String getFirstIfPresent(String[] inputList) {
        if (inputList == null || inputList.length == 0) {
            return null;
        }
        return inputList[0];
    }


//    public static ServiceRequest buildServiceRequest(MultivaluedMap<String, String> queryParams) {
    public static ServiceRequest buildServiceRequest(Map<String, String[]> queryParams) {
        ServiceRequest serviceRequest = new ServiceRequest();
        Map<String, List<String>> parameters = new HashMap<>();
        serviceRequest.setQ(getFirstIfPresent(queryParams.get(Q)));
        serviceRequest.setFq(getFirstIfPresent(queryParams.get(FQ)));
        serviceRequest.setQt(getFirstIfPresent(queryParams.get(GlobalConstants.QT)));
        String rows = getFirstIfPresent(queryParams.get(ROWS));
        String facetFieldParam = getFirstIfPresent(queryParams.get(FACET_FIELDS));
        String facetSortParam = getFirstIfPresent(queryParams.get(FACET_SORT));
        String groupFieldParam = getFirstIfPresent(queryParams.get(GROUP_FIELDS));

        String sort = getFirstIfPresent(queryParams.get(SORT));
        if (StringUtils.isNoneEmpty(sort)) {
            serviceRequest.setSort(sort);
        }
        String sortOrder = getFirstIfPresent(queryParams.get(SORT_ORDER));
        if (StringUtils.isNoneEmpty(sortOrder)) {
            serviceRequest.setSortOrder(sortOrder);
        }
        if (StringUtils.isNumeric(rows)) {
            serviceRequest.setRows(Integer.parseInt(rows));
        }
        String start = getFirstIfPresent(queryParams.get(START));
        if (StringUtils.isNumeric(start)) {
            serviceRequest.setStart(Integer.parseInt(start));
        }

        if (StringUtils.isNotEmpty(facetFieldParam)) {
            serviceRequest.setFacetFields(facetFieldParam.split(COMMA));
        }

        if (StringUtils.isNotEmpty(groupFieldParam)) {
            serviceRequest.setGroupFields(groupFieldParam.split(COMMA));
        }

        if (StringUtils.isNotEmpty(facetSortParam)) {
            serviceRequest.setFacetSort(facetSortParam);
        }
        String fromParamValue = getFirstIfPresent(queryParams.get(FROM));
        if (StringUtils.isNotEmpty(fromParamValue)) {
            serviceRequest.setFrom(From.getFrom(fromParamValue));
        }

        String debug = getFirstIfPresent(queryParams.get(DEBUG));
        Set<String> parameterNames = queryParams.keySet();
        for (String paramName : parameterNames) {
            if (!GlobalConstants.KNOWN_PARAMETERS.contains(paramName)) {
                parameters.put(paramName, Arrays.asList(queryParams.get(paramName)));
            }
        }
        serviceRequest.setParameters(parameters);
        if (StringUtils.isNotEmpty(debug) && debug.equals(TRUE)) {
            serviceRequest.setDebug(true);
            logger.info(serviceRequest);
        }
        return serviceRequest;
    }
}
