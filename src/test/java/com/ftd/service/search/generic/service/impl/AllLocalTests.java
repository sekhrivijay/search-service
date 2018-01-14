package com.ftd.service.search.generic.service.impl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ftd.service.search.bl.details.AvailabilityClientTest;
import com.ftd.service.search.bl.details.PricingClientTest;
import com.ftd.service.search.bl.details.ProductClientTest;
import com.ftd.service.search.bl.impl.DetailsTest;

@RunWith(Suite.class)
@SuiteClasses({ DetailsTest.class, AvailabilityClientTest.class, PricingClientTest.class, ProductClientTest.class })
public class AllLocalTests {

}
