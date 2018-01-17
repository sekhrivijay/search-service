package com.ftd.services.search.generic.service.impl;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import java.net.URL;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.ftd.services.search.ServiceApplication;
import com.ftd.services.search.api.response.SearchServiceResponse;

/**
 * General failures of these tests may be correctable by adding
 * -Dspring.profiles.active=dev1. "dev1" is a value that can be found in the
 * resources folder as a suffix to one of the spring configuration application-?
 * files.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ServiceApplication.class)
public class QueryServiceImplTest {

    @LocalServerPort
    private int              port;

    private URL              base;

    @Inject
    private TestRestTemplate template;

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/api/");
    }

    @Test
    public void search() throws Exception {
        final Long expectedNumberFound = new Long(0);
        ResponseEntity<SearchServiceResponse> response = template.getForEntity(
                base.toString() + "search?q=*:*",
                SearchServiceResponse.class);
        Assert.assertEquals("checking for 200 OK", OK, response.getStatusCode());
//        Assert.assertTrue("number found should be > 0 for search",
//                expectedNumberFound.longValue() < response.getBody().getNumFound().longValue());
    }

    @Test
    public void details() throws Exception {
        final Long expectedNumberFound = new Long(0);
        ResponseEntity<SearchServiceResponse> response = template.getForEntity(
                base.toString() + "details?q=*:*"
                        + "&siteId=ftd&memberType=WHAT&availFrom=2018-01-01&availTo=2018-01-02&zipCode=60532",
                SearchServiceResponse.class);
        Assert.assertEquals("checking for 200 OK", OK, response.getStatusCode());
//        Assert.assertTrue("number found should be > 0 for details",
//                expectedNumberFound.longValue() < response.getBody().getNumFound().longValue());
    }

//    @Test
//    public void browse() throws Exception {
//        ResponseEntity<SearchServiceResponse> response = template.getForEntity(
//                base.toString() + "browse?q=*:*",
//                SearchServiceResponse.class);
//        Assert.assertEquals("checking for page not found", NOT_FOUND, response.getStatusCode());
//    }

    @Test
    public void autofill() throws Exception {
        ResponseEntity<SearchServiceResponse> response = template.getForEntity(
                base.toString() + "autofill?q=*:*",
                SearchServiceResponse.class);
        Assert.assertEquals("checking for 200 OK", OK, response.getStatusCode());
    }

    @Test
    public void spell() throws Exception {
        ResponseEntity<SearchServiceResponse> response = template.getForEntity(
                base.toString() + "spell",
                SearchServiceResponse.class);
        Assert.assertEquals("checking for 200 OK", OK, response.getStatusCode());
    }

    @Test
    public void pdp() throws Exception {
        ResponseEntity<SearchServiceResponse> response = template.getForEntity(
                base.toString() + "pdp",
                SearchServiceResponse.class);
        Assert.assertEquals("checking for page not found", NOT_FOUND, response.getStatusCode());
    }

}