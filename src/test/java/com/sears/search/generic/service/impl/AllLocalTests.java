package com.sears.search.generic.service.impl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.micro.services.search.bl.details.AvailabilityClientTest;
import com.micro.services.search.bl.details.PricingClientTest;
import com.micro.services.search.bl.details.ProductClientTest;

@RunWith(Suite.class)
@SuiteClasses({ AvailabilityClientTest.class, PricingClientTest.class, ProductClientTest.class })
public class AllLocalTests {

}
