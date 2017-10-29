package com.micro.services.search.bl.processor;

import com.micro.services.search.api.request.SearchServiceRequest;
import com.micro.services.search.api.response.DidYouMean;
import com.micro.services.search.api.response.SearchServiceResponse;
import com.micro.services.search.config.GlobalConstants;
import com.micro.services.search.util.SpellCorrectUtil;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.jetbrains.annotations.NotNull;
import org.languagetool.rules.RuleMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named("didYouMeanDelegate")
public class DidYouMeanDelegate extends BaseDelegate {
    private static final Logger LOGGER = LoggerFactory.getLogger(DidYouMeanDelegate.class);

    @Autowired
    private SpellCorrectUtil spellCorrectUtil;

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        return solrQuery;
    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest,
                                                   QueryResponse queryResponse,
                                                   SearchServiceResponse searchServiceResponse) {

        if(queryResponse == null
                || queryResponse.getResults() == null
                || queryResponse.getResults().getNumFound() > 0
                || searchServiceRequest == null
                || searchServiceRequest.getQ() == null) {
            return searchServiceResponse;
        }

        List<DidYouMean> didYouMeanList = getDidYouMeansFromJLanguageTools(searchServiceResponse, searchServiceRequest.getQ());
        queryResponse.getSpellCheckResponse();


        searchServiceResponse.setDidYouMeanList(didYouMeanList);
        return searchServiceResponse;
    }



    @NotNull
    private List<DidYouMean> getDidYouMeansFromJLanguageTools(SearchServiceResponse searchServiceResponse, String term) {
        List<RuleMatch> ruleMatchList = spellCorrectUtil.getRuleMatchList(term);
        List<DidYouMean> didYouMeanList = new ArrayList<>();
        for (RuleMatch ruleMatch : ruleMatchList) {
            List<String> suggestedReplacements = ruleMatch.getSuggestedReplacements();
            for(String suggestion: suggestedReplacements) {
                DidYouMean didYouMean = new DidYouMean();
                didYouMean.setSuggestedTerm(suggestion);
                didYouMean.setUrl(searchServiceResponse.getOriginalQuery() + "&q=" + suggestion);
                didYouMeanList.add(didYouMean);
            }
        }
        return didYouMeanList;
    }


    public  List<DidYouMean> getDidYouMeansFromSolr(SpellCheckResponse spellCheckResponse, SearchServiceResponse searchServiceResponse, String term) {
        List<DidYouMean> didYouMeanList = new ArrayList<>();
        if (!term.contains(GlobalConstants.SPACE) || spellCheckResponse.getCollatedResults() == null || spellCheckResponse.getCollatedResults().size() == 0) {
            List<SpellCheckResponse.Suggestion> suggestions = spellCheckResponse.getSuggestions();
            if (suggestions != null) {
                for (SpellCheckResponse.Suggestion suggestion : suggestions) {
                    List<String> alternatives = suggestion.getAlternatives();
                    if (alternatives != null) {
                        for (String alternative : alternatives) {
                            if (!alternative.contains(GlobalConstants.SPACE)) {
                                LOGGER.info("Spell check alternative " + alternative);
                                DidYouMean didYouMean = new DidYouMean();
                                didYouMean.setSuggestedTerm(alternative);
                                spellCorrectedServiceRequest.setIsSpellCheck(true);
                                return spellCorrectedServiceRequest;
                            }
                        }
                    }
                }
            }
        } else {
            List<SpellCheckResponse.Collation> collations = spellCheckResponse.getCollatedResults();
            if (collations != null) {
                for (SpellCheckResponse.Collation collation : collations) {
                    LOGGER.info("Spell check collation " + collation.getCollationQueryString());
                    spellCorrectedServiceRequest.setTerm(collation.getCollationQueryString());
                    spellCorrectedServiceRequest.setIsSpellCheck(true);
                    return spellCorrectedServiceRequest;
                }
            }
        }
    }

}