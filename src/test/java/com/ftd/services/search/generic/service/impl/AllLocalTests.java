package com.ftd.services.search.generic.service.impl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ftd.services.search.bl.details.AvailabilityClientTest;
import com.ftd.services.search.bl.details.PricingClientTest;
import com.ftd.services.search.bl.details.ProductClientTest;
import com.ftd.services.search.bl.impl.DetailsTest;

@RunWith(Suite.class)
@SuiteClasses({ DetailsTest.class, AvailabilityClientTest.class, PricingClientTest.class, ProductClientTest.class })
public class AllLocalTests {

}
