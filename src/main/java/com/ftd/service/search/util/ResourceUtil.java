package com.ftd.service.search.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ftd.service.search.api.request.Domain;
import com.ftd.service.search.api.request.From;
import com.ftd.service.search.api.request.RequestType;
import com.ftd.service.search.api.request.SearchServiceRequest;
import com.ftd.service.search.config.GlobalConstants;

public class ResourceUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceUtil.class);

    public static String getFirstIfPresent(String[] inputList) {
        if (inputList == null || inputList.length == 0) {
            return null;
        }
        return inputList[0];
    }


    public static SearchServiceRequest buildServiceRequest(Map<String, String[]> queryParams) {
        SearchServiceRequest searchServiceRequest = new SearchServiceRequest();
        Map<String, List<String>> parameters = new HashMap<>();
        Map<String, String[]> originalParameters = new HashMap<>();
        searchServiceRequest.setQ(getFirstIfPresent(queryParams.get(GlobalConstants.Q)));
        searchServiceRequest.setFq(queryParams.get(GlobalConstants.FQ));
        searchServiceRequest.setQt(getFirstIfPresent(queryParams.get(GlobalConstants.QT)));
        String rows = getFirstIfPresent(queryParams.get(GlobalConstants.ROWS));
        String facetFieldParam = getFirstIfPresent(queryParams.get(GlobalConstants.FACET_FIELDS));
        String facetSortParam = getFirstIfPresent(queryParams.get(GlobalConstants.FACET_SORT));
        String groupFieldParam = getFirstIfPresent(queryParams.get(GlobalConstants.GROUP_FIELDS));

        String type = getFirstIfPresent(queryParams.get(GlobalConstants.TYPE));
        if (StringUtils.isNotEmpty(type)) {
            searchServiceRequest.setRequestType(RequestType.getRequestType(type));
        }

        String domain = getFirstIfPresent(queryParams.get(GlobalConstants.DOMAIN));
        if (StringUtils.isNotEmpty(domain)) {
            searchServiceRequest.setDomain(Domain.getDomain(domain));
        }

        String siteId = getFirstIfPresent(queryParams.get(GlobalConstants.SITE_ID));
        if (StringUtils.isNotEmpty(siteId)) {
            searchServiceRequest.setSiteId(siteId);
        }

        String value = getFirstIfPresent(queryParams.get(GlobalConstants.MEMBER_TYPE));
        if (StringUtils.isNotEmpty(value)) {
            searchServiceRequest.setMemberType(value);
        }
        value = getFirstIfPresent(queryParams.get(GlobalConstants.ZIPCODE));
        if (StringUtils.isNotEmpty(value)) {
            searchServiceRequest.setZipCode(value);
        }
        value = getFirstIfPresent(queryParams.get(GlobalConstants.AVAIL_FROM));
        if (StringUtils.isNotEmpty(value)) {
            searchServiceRequest.setAvailFrom(value);
        }
        value = getFirstIfPresent(queryParams.get(GlobalConstants.AVAIL_TO));
        if (StringUtils.isNotEmpty(value)) {
            searchServiceRequest.setAvailTo(value);
        }

        String sort = getFirstIfPresent(queryParams.get(GlobalConstants.SORT));
        if (StringUtils.isNotEmpty(sort)) {
            searchServiceRequest.setSort(sort);
        }
        String sortOrder = getFirstIfPresent(queryParams.get(GlobalConstants.SORT_ORDER));
        if (StringUtils.isNotEmpty(sortOrder)) {
            searchServiceRequest.setSortOrder(sortOrder);
        }
        if (StringUtils.isNumeric(rows)) {
            searchServiceRequest.setRows(Integer.parseInt(rows));
        }
        String start = getFirstIfPresent(queryParams.get(GlobalConstants.START));
        if (StringUtils.isNumeric(start)) {
            searchServiceRequest.setStart(Integer.parseInt(start));
        }

        if (StringUtils.isNotEmpty(facetFieldParam)) {
            searchServiceRequest.setFacetFields(facetFieldParam.split(GlobalConstants.COMMA));
        }

        if (StringUtils.isNotEmpty(groupFieldParam)) {
            searchServiceRequest.setGroupFields(groupFieldParam.split(GlobalConstants.COMMA));
        }

        if (StringUtils.isNotEmpty(facetSortParam)) {
            searchServiceRequest.setFacetSort(facetSortParam);
        }
        String fromParamValue = getFirstIfPresent(queryParams.get(GlobalConstants.FROM));
        if (StringUtils.isNotEmpty(fromParamValue)) {
            searchServiceRequest.setFrom(From.getFrom(fromParamValue));
        }

        String debug = getFirstIfPresent(queryParams.get(GlobalConstants.DEBUG));
        Set<String> parameterNames = queryParams.keySet();
        for (String paramName : parameterNames) {
            if (!GlobalConstants.KNOWN_PARAMETERS.contains(paramName)) {
                parameters.put(paramName, Arrays.asList(queryParams.get(paramName)));
            }
        }
        searchServiceRequest.setParameters(parameters);

        for (String paramName : parameterNames) {
            originalParameters.put(paramName, queryParams.get(paramName));
        }
        searchServiceRequest.setParametersOriginal(originalParameters);
        if (StringUtils.isNotEmpty(debug) && debug.equals(GlobalConstants.TRUE)) {
            searchServiceRequest.setDebug(true);
            LOGGER.info(searchServiceRequest.toString());
        }
        return searchServiceRequest;
    }
}
