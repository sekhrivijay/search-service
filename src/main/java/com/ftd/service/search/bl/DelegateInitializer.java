package com.ftd.service.search.bl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ftd.service.search.api.request.RequestType;
import com.ftd.service.search.api.request.SearchServiceRequest;
import com.ftd.service.search.bl.processor.DebugDelegate;
import com.ftd.service.search.bl.processor.Delegate;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
public class DelegateInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DelegateInitializer.class);
    private Delegate queryTermDelegate;

    private Delegate documentsDelegate;
    private Delegate timeAllowedDelegate;
    private Delegate filterDelegate;
    private Delegate sortDelegate;
    private Delegate rowsPreDelegate;
    private Delegate rowsPostDelegate;
    private Delegate startDelegate;
    private Delegate parameterDelegate;
    private Delegate numFoundDelegate;
    private Delegate facetDelegate;
    private Delegate groupDelegate;
    private Delegate paginationDelegate;
    private Delegate requestHandlerDelegate;
    private Delegate siteDelegate;
//    private Delegate breadCrumbDelegate;
    private Delegate didYouMeanDelegate;
    private Delegate fuzzySearchDelegate;
    private Delegate mustMatchDelegate;
    private Delegate rulesDelegate;
    private Delegate boostDelegate;


    public Map<String, List<Delegate>> buildDelegateMapList(SearchServiceRequest searchServiceRequest) {
        List<Delegate> mainDelegateList = new ArrayList<>();
        mainDelegateList.add(queryTermDelegate);
        mainDelegateList.add(requestHandlerDelegate);

        mainDelegateList.add(timeAllowedDelegate);
        mainDelegateList.add(documentsDelegate);
        mainDelegateList.add(filterDelegate);
        mainDelegateList.add(siteDelegate);
//        mainDelegateList.add(breadCrumbDelegate);
        mainDelegateList.add(rowsPreDelegate);
        mainDelegateList.add(parameterDelegate);

        if (searchServiceRequest.getRequestType() == RequestType.SEARCH
                || searchServiceRequest.getRequestType() == RequestType.BROWSE) {
            addSecondaryDelegates(mainDelegateList);
        }
        if (searchServiceRequest.getRequestType() == RequestType.SPELL) {
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
        mainDelegateList.add(rulesDelegate);
        mainDelegateList.add(boostDelegate);
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


    @Inject
    @Named("queryTermDelegate")
    public void setQueryTermDelegate(Delegate queryTermDelegate) {
        this.queryTermDelegate = queryTermDelegate;
    }

    @Inject
    @Named("fuzzySearchDelegate")
    public void setFuzzySearchDelegate(Delegate fuzzySearchDelegate) {
        this.fuzzySearchDelegate = fuzzySearchDelegate;
    }

    @Inject
    @Named("requestHandlerDelegate")
    public void setRequestHandlerDelegate(Delegate requestHandlerDelegate) {
        this.requestHandlerDelegate = requestHandlerDelegate;
    }

    @Inject
    @Named("documentsDelegate")
    public void setDocumentsDelegate(Delegate documentsDelegate) {
        this.documentsDelegate = documentsDelegate;
    }

    @Inject
    @Named("timeAllowedDelegate")
    public void setTimeAllowedDelegate(Delegate timeAllowedDelegate) {
        this.timeAllowedDelegate = timeAllowedDelegate;
    }

    @Inject
    @Named("filterDelegate")
    public void setFilterDelegate(Delegate filterDelegate) {
        this.filterDelegate = filterDelegate;
    }

    @Inject
    @Named("sortDelegate")
    public void setSortDelegate(Delegate sortDelegate) {
        this.sortDelegate = sortDelegate;
    }

    @Inject
    @Named("rowsPreDelegate")
    public void setRowsPreDelegate(Delegate rowsPreDelegate) {
        this.rowsPreDelegate = rowsPreDelegate;
    }

    @Inject
    @Named("rowsPostDelegate")
    public void setRowsPostDelegate(Delegate rowsPostDelegate) {
        this.rowsPostDelegate = rowsPostDelegate;
    }

    @Inject
    @Named("startDelegate")
    public void setStartDelegate(Delegate startDelegate) {
        this.startDelegate = startDelegate;
    }

    @Inject
    @Named("parameterDelegate")
    public void setParameterDelegate(Delegate parameterDelegate) {
        this.parameterDelegate = parameterDelegate;
    }

    @Inject
    @Named("numFoundDelegate")
    public void setNumFoundDelegate(Delegate numFoundDelegate) {
        this.numFoundDelegate = numFoundDelegate;
    }

    @Inject
    @Named("facetDelegate")
    public void setFacetDelegate(Delegate facetDelegate) {
        this.facetDelegate = facetDelegate;
    }

    @Inject
    @Named("groupDelegate")
    public void setGroupDelegate(Delegate groupDelegate) {
        this.groupDelegate = groupDelegate;
    }

    @Inject
    @Named("paginationDelegate")
    public void setPaginationDelegate(Delegate paginationDelegate) {
        this.paginationDelegate = paginationDelegate;
    }

    @Inject
    @Named("didYouMeanDelegate")
    public void setDidYouMeanDelegate(Delegate didYouMeanDelegate) {
        this.didYouMeanDelegate = didYouMeanDelegate;
    }

    @Inject
    @Named("mustMatchDelegate")
    public void setMustMatchDelegate(Delegate mustMatchDelegate) {
        this.mustMatchDelegate = mustMatchDelegate;
    }

    @Inject
    @Named("rulesDelegate")
    public void setRulesDelegate(Delegate rulesDelegate) {
        this.rulesDelegate = rulesDelegate;
    }

    @Inject
    @Named("siteDelegate")
    public void setSiteDelegate(Delegate siteDelegate) {
        this.siteDelegate = siteDelegate;
    }

    @Inject
    @Named("boostDelegate")
    public void setBoostDelegate(Delegate boostDelegate) {
        this.boostDelegate = boostDelegate;
    }
}