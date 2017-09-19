package com.sears.search.generic.service.impl;

import com.micro.services.search.api.response.ServiceResponse;
import com.micro.services.search.main.ServiceApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.net.URL;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = ServiceApplication.class)
public class QueryServiceImplTest {

    @Value("${server.port:8080}")
    private int port;

    private URL base;

    @Inject
    private TestRestTemplate template;

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/deals/search?q=*:*");
    }

    @Test
    public void testGetHello() throws Exception {
        ResponseEntity<ServiceResponse> response = template.getForEntity(base.toString(), ServiceResponse.class);
        System.out.println(response);
//        assertThat(response.getBody(), equalTo("Greetings from Spring Boot!"));
    }

}