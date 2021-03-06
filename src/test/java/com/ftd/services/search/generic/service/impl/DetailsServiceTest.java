package com.ftd.services.search.generic.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ftd.services.search.ServiceApplication;
import com.ftd.services.search.bl.DetailsService;

/**
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceApplication.class)
public class DetailsServiceTest {

    @Autowired
    private DetailsService detailsService;

    @Test
    public void basicCall() throws Exception {
        detailsService.postQueryDetails(null, null);
    }
}