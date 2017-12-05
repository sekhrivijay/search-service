package com.micro.services.search.resource;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.micro.services.search.api.request.RequestType;
import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.SearchServiceResponse;
import com.micro.services.search.bl.QueryService;
import com.micro.services.search.util.ResourceUtil;
import com.services.micro.commons.logging.annotation.LogExecutionTime;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@RefreshScope
@RequestMapping("/api")
public class ServiceResource {
    private QueryService queryService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceResource.class.getName());

    public ServiceResource() {
    }

    @Autowired
    public void setQueryService(QueryService queryService) {
        this.queryService = queryService;
    }

    @Timed
    @ExceptionMetered
    @ApiOperation(
            value = "Get search results ",
            notes = "Pass q and other parameters to get relevant suggestions back ",
            response = SearchServiceResponse.class
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "q",
                    value = "Search keyword parameter ",
                    required = true,
                    dataType = "string",
                    paramType = "query"),
            @ApiImplicitParam(name = "fq",
                    value = "Provide solr filter capabilities ",
                    required = false,
                    dataType = "string",
                    paramType = "query"),
            @ApiImplicitParam(name = "qt",
                    value = "Provide solr handler capabilities ",
                    required = false,
                    dataType = "string",
                    paramType = "query"),
            @ApiImplicitParam(name = "facet.fields",
                    value = "comma seperated list of fields that we need to return facet on ",
                    required = false,
                    dataType = "string",
                    paramType = "query"),
            @ApiImplicitParam(name = "facet.sort",
                    value = "To sort facet response",
                    required = false,
                    dataType = "string",
                    paramType = "query"),
            @ApiImplicitParam(name = "rows",
                    value = "To limit the number of rows returned",
                    required = false,
                    dataType = "integer",
                    paramType = "query"),
            @ApiImplicitParam(name = "start",
                    value = "To return documents starting from that number . To help with pagination",
                    required = false,
                    dataType = "integer",
                    paramType = "query"),
            @ApiImplicitParam(name = "from",
                    value = "To bypass cache by sending value from=index",
                    required = false,
                    dataType = "string",
                    paramType = "query"),
            @ApiImplicitParam(name = "sort",
                    value = "To sort the returned response",
                    required = false,
                    dataType = "string",
                    paramType = "query"),
            @ApiImplicitParam(name = "sort.order",
                    value = "To modify the sort order as asc and desc",
                    required = false,
                    dataType = "string",
                    paramType = "query"),
            @ApiImplicitParam(name = "type",
                    value = "To determine what the different request types are. " +
                            "Valid examples type=[search, browse, autofill, spell, pdp] ",
                    required = false,
                    dataType = "string",
                    paramType = "query"),
            @ApiImplicitParam(name = "site",
                    value = "To determine how the response is returned. Valid examples site=[desktop, mobile]",
                    required = false,
                    dataType = "string",
                    paramType = "query"),
            @ApiImplicitParam(name = "debug",
                    value = "To enable debugging ",
                    required = false,
                    dataType = "string",
                    paramType = "query"),
    })
    @GetMapping("/search")
    @LogExecutionTime
    public SearchServiceResponse search(WebRequest webRequest) throws Exception {
        SearchServiceRequest searchServiceRequest = ResourceUtil.buildServiceRequest(webRequest.getParameterMap());
        searchServiceRequest.setRequestType(RequestType.SEARCH);
        return queryService.query(searchServiceRequest);
    }

    @ApiOperation(
            value = "Get browse results ",
            notes = "Pass fq and other parameters to get relevant results back ",
            response = SearchServiceResponse.class
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "q",
                    value = "Search keyword parameter ",
                    required = true,
                    dataType = "string",
                    paramType = "query"),
            @ApiImplicitParam(name = "qt",
                    value = "Provide solr handler capabilities ",
                    required = false,
                    dataType = "string",
                    paramType = "query"),
            @ApiImplicitParam(name = "fq",
                    value = "Provide solr filter capabilities ",
                    required = false,
                    dataType = "string",
                    paramType = "query"),
            @ApiImplicitParam(name = "facet.sort",
                    value = "To sort facet response",
                    required = false,
                    dataType = "string",
                    paramType = "query"),
            @ApiImplicitParam(name = "facet.fields",
                    value = "comma seperated list of fields that we need to return facet on ",
                    required = false,
                    dataType = "string",
                    paramType = "query"),
            @ApiImplicitParam(name = "rows",
                    value = "To limit the number of rows returned",
                    required = false,
                    dataType = "integer",
                    paramType = "query"),
            @ApiImplicitParam(name = "start",
                    value = "To return documents starting from that number . To help with pagination",
                    required = false,
                    dataType = "integer",
                    paramType = "query"),
            @ApiImplicitParam(name = "sort",
                    value = "To sort the returned response",
                    required = false,
                    dataType = "string",
                    paramType = "query"),
            @ApiImplicitParam(name = "sort.order",
                    value = "To modify the sort order as asc and desc",
                    required = false,
                    dataType = "string",
                    paramType = "query"),
            @ApiImplicitParam(name = "site",
                    value = "To determine how the response is returned. Valid examples site=[desktop, mobile]",
                    required = false,
                    dataType = "string",
                    paramType = "query"),
            @ApiImplicitParam(name = "from",
                    value = "To bypass cache by sending value from=index",
                    required = false,
                    dataType = "string",
                    paramType = "query"),
            @ApiImplicitParam(name = "debug",
                    value = "To enable debugging ",
                    required = false,
                    dataType = "string",
                    paramType = "query"),
    })
    @GetMapping("/browse")
    @LogExecutionTime
    @Timed
    @ExceptionMetered
    public SearchServiceResponse browse(WebRequest webRequest) throws Exception {
        SearchServiceRequest searchServiceRequest = ResourceUtil.buildServiceRequest(webRequest.getParameterMap());
        searchServiceRequest.setRequestType(RequestType.BROWSE);
        return queryService.query(searchServiceRequest);
    }


    @ApiOperation(
            value = "Get type ahead results ",
            notes = "Pass q and other parameters to get relevant suggestions back ",
            response = SearchServiceResponse.class
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "q",
                    value = "Search keyword parameter ",
                    required = true,
                    dataType = "string",
                    paramType = "query"),
            @ApiImplicitParam(name = "site",
                    value = "To determine how the response is returned. Valid examples site=[desktop, mobile]",
                    required = false,
                    dataType = "string",
                    paramType = "query"),
            @ApiImplicitParam(name = "from",
                    value = "To bypass cache by sending value from=index",
                    required = false,
                    dataType = "string",
                    paramType = "query"),
            @ApiImplicitParam(name = "debug",
                    value = "To enable debugging ",
                    required = false,
                    dataType = "string",
                    paramType = "query"),
    })
    @GetMapping("/autofill")
    @LogExecutionTime
    @Timed
    @ExceptionMetered
    public SearchServiceResponse autofill(WebRequest webRequest) throws Exception {
        SearchServiceRequest searchServiceRequest = ResourceUtil.buildServiceRequest(webRequest.getParameterMap());
        searchServiceRequest.setRequestType(RequestType.AUTOFILL);
        return queryService.queryAutofill(searchServiceRequest);
    }

    @ApiOperation(
            value = "Get product detail results ",
            notes = "Pass fq and other parameters to get relevant result back ",
            response = SearchServiceResponse.class
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fq",
                    value = "Provide solr filter capabilities ",
                    required = false,
                    dataType = "string",
                    paramType = "query"),
            @ApiImplicitParam(name = "site",
                    value = "To determine how the response is returned. Valid examples site=[desktop, mobile]",
                    required = false,
                    dataType = "string",
                    paramType = "query"),
            @ApiImplicitParam(name = "from",
                    value = "To bypass cache by sending value from=index",
                    required = false,
                    dataType = "string",
                    paramType = "query"),
            @ApiImplicitParam(name = "debug",
                    value = "To enable debugging ",
                    required = false,
                    dataType = "string",
                    paramType = "query"),
    })
    @GetMapping("/product")
    @LogExecutionTime
    @Timed
    @ExceptionMetered
    public SearchServiceResponse product(WebRequest webRequest) throws Exception {
        SearchServiceRequest searchServiceRequest = ResourceUtil.buildServiceRequest(webRequest.getParameterMap());
        searchServiceRequest.setRequestType(RequestType.PDP);
        return queryService.query(searchServiceRequest);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "q",
                    value = "Search keyword parameter ",
                    required = true,
                    dataType = "string",
                    paramType = "query"),
            @ApiImplicitParam(name = "site",
                    value = "To determine how the response is returned. Valid examples site=[desktop, mobile]",
                    required = false,
                    dataType = "string",
                    paramType = "query"),
            @ApiImplicitParam(name = "debug",
                    value = "To enable debugging ",
                    required = false,
                    dataType = "string",
                    paramType = "query"),
            @ApiImplicitParam(name = "from",
                    value = "To bypass cache by sending value from=index",
                    required = false,
                    dataType = "string",
                    paramType = "query"),
    })
    @ApiOperation(
            value = "Get spell correction results ",
            notes = "Pass q and other parameters to get relevant suggestions back ",
            response = SearchServiceResponse.class
    )
    @GetMapping("/spell")
    @LogExecutionTime
    @Timed
    @ExceptionMetered
    public SearchServiceResponse spell(WebRequest webRequest) throws Exception {
        SearchServiceRequest searchServiceRequest = ResourceUtil.buildServiceRequest(webRequest.getParameterMap());
        searchServiceRequest.setRequestType(RequestType.SPELL);
        return queryService.queryAutofill(searchServiceRequest);
    }

}
