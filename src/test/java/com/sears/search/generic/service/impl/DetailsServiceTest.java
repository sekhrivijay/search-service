package com.sears.search.generic.service.impl;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.micro.services.search.ServiceApplication;
import com.micro.services.search.bl.DetailsService;

/**
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceApplication.class)
public class DetailsServiceTest {

    @Inject
    private DetailsService detailsService;

    @Test
    public void basicCall() throws Exception {
        detailsService.postQueryDetails(null, null);
    }
}