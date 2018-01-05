package com.micro.services.search.controller;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@RequestMapping("/api2")
public class OrchestrationController  {

//    @GetMapping("${service.searchEndpoint}")
//    @LogExecutionTime
//    public SearchServiceResponse searchOrchestrate(WebRequest webRequest) throws Exception {
//        SearchServiceResponse searchServiceResponse = search(webRequest);
//
//        return searchServiceResponse;
//    }
}
