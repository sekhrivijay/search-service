package com.micro.services.search.bl;

import com.micro.services.search.api.request.RequestType;
import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.bl.processor.DebugDelegate;
import com.micro.services.search.bl.processor.Delegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
public class DelegateInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DelegateInitializer.class);

    @Inject
    @Named("queryTermDelegate")
    private Delegate queryTermDelegate;

    @Inject
    @Named("fuzzySearchDelegate")
    private Delegate fuzzySearchDelegate;
    @Inject
    @Named("requestHandlerDelegate")
    private Delegate requestHandlerDelegate;
    @Inject
    @Named("productsDelegate")
    private Delegate productsDelegate;
    @Inject
    @Named("timeAllowedDelegate")
    private Delegate timeAllowedDelegate;
    @Inject
    @Named("filterDelegate")
    private Delegate filterDelegate;
    @Inject
    @Named("sortDelegate")
    private Delegate sortDelegate;
    @Inject
    @Named("rowsPreDelegate")
    private Delegate rowsPreDelegate;
    @Inject
    @Named("rowsPostDelegate")
    private Delegate rowsPostDelegate;
    @Inject
    @Named("startDelegate")
    private Delegate startDelegate;
    @Inject
    @Named("parameterDelegate")
    private Delegate parameterDelegate;
    @Inject
    @Named("numFoundDelegate")
    private Delegate numFoundDelegate;
    @Inject
    @Named("facetDelegate")
    private Delegate facetDelegate;
    @Inject
    @Named("groupDelegate")
    private Delegate groupDelegate;
    @Inject
    @Named("paginationDelegate")
    private Delegate paginationDelegate;
//    @Inject
//    @Named("breadCrumbDelegate")
//    private Delegate breadCrumbDelegate;
    @Inject
    @Named("didYouMeanDelegate")
    private Delegate didYouMeanDelegate;

    @Inject
    @Named("mustMatchDelegate")
    private Delegate mustMatchDelegate;


    public Map<String, List<Delegate>> buildDelegateMapList(SearchServiceRequest searchServiceRequest) {
        List<Delegate> mainDelegateList = new ArrayList<>();
        mainDelegateList.add(queryTermDelegate);
        mainDelegateList.add(requestHandlerDelegate);

        mainDelegateList.add(timeAllowedDelegate);
        mainDelegateList.add(productsDelegate);
        mainDelegateList.add(filterDelegate);
//        mainDelegateList.add(breadCrumbDelegate);
        mainDelegateList.add(rowsPreDelegate);
        mainDelegateList.add(parameterDelegate);

        if (searchServiceRequest.getRequestType() == RequestType.SEARCH
                || searchServiceRequest.getRequestType() == RequestType.BROWSE) {
            addSecondaryDelegates(mainDelegateList);
        }
        if(searchServiceRequest.getRequestType() == RequestType.SPELL) {
            mainDelegateList.add(didYouMeanDelegate);
        }
        Map<String, List<Delegate>> delegateMapList = new HashMap<>();

        if (searchServiceRequest.isDebug()) {
            mainDelegateList.add(new DebugDelegate());
            LOGGER.info("Delegate Map List is " + mainDelegateList);
        }
        delegateMapList.put("", mainDelegateList);
        return delegateMapList;

    }

    private void addSecondaryDelegates(List<Delegate> mainDelegateList) {
        mainDelegateList.add(fuzzySearchDelegate);
        mainDelegateList.add(facetDelegate);
        mainDelegateList.add(startDelegate);
        mainDelegateList.add(numFoundDelegate);
        mainDelegateList.add(mustMatchDelegate);
        mainDelegateList.add(rowsPostDelegate);
        mainDelegateList.add(groupDelegate);
        mainDelegateList.add(paginationDelegate);
        mainDelegateList.add(sortDelegate);
        mainDelegateList.add(didYouMeanDelegate);
    }

}