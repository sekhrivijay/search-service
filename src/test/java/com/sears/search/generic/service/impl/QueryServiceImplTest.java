package com.sears.search.generic.service.impl;

import static org.springframework.http.HttpStatus.OK;

import java.net.URL;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.micro.services.search.ServiceApplication;
import com.micro.services.search.api.response.SearchServiceResponse;

/**
 * General failures of these tests may be correctable by adding
 * -Dspring.profiles.active=dev1. "dev1" is a value that can be found in the
 * resources folder as a suffix to one of the spring configuration application-?
 * files.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = ServiceApplication.class)
public class QueryServiceImplTest {

    @Value("${server.port:8080}")
    private int              port;

    private URL              base;

    @Inject
    private TestRestTemplate template;

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/api/search");
    }

    @Test
    public void searchForEverything() throws Exception {
        final Long expectedNumberFound = new Long(17);
        ResponseEntity<SearchServiceResponse> response = template.getForEntity(
                base.toString() + "?q=*:*",
                SearchServiceResponse.class);
        Assert.assertEquals("checking for 200 OK", OK, response.getStatusCode());
        Assert.assertEquals("number found", expectedNumberFound, response.getBody().getNumFound());
    }

    @Test
    public void searchForNothing2() throws Exception {
        final Long expectedNumberFound = new Long(13);
        ResponseEntity<SearchServiceResponse> response = template.getForEntity(
                base.toString() + "?q=:*",
                SearchServiceResponse.class);
        Assert.assertEquals("checking for 200 OK", OK, response.getStatusCode());
        Assert.assertEquals("number found", expectedNumberFound, response.getBody().getNumFound());
    }

    @Test
    public void searchForNothing() throws Exception {
        final Long expectedNumberFound = new Long(13);
        ResponseEntity<SearchServiceResponse> response = template.getForEntity(
                base.toString(),
                SearchServiceResponse.class);
        Assert.assertEquals("checking for 200 OK", OK, response.getStatusCode());
        Assert.assertEquals("number found", expectedNumberFound, response.getBody().getNumFound());
    }

    @Test
    public void searchForNonExistentProducts() throws Exception {
        final Long expectedNumberFound = new Long(0);
        ResponseEntity<SearchServiceResponse> response = template.getForEntity(
                base.toString() + "?q=ThisIsNotAProduct",
                SearchServiceResponse.class);
        Assert.assertEquals("checking for 200 OK", OK, response.getStatusCode());
        Assert.assertEquals("number found", expectedNumberFound, response.getBody().getNumFound());
    }

}