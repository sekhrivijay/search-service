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
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named("didYouMeanDelegate")
public class DidYouMeanDelegate extends BaseDelegate {
    private static final Logger LOGGER = LoggerFactory.getLogger(DidYouMeanDelegate.class);

    @Autowired
    private SpellCorrectUtil spellCorrectUtil;

    @Value("${service.spellCheckNumfoundThreshhold:0}")
    private long spellCheckNumfoundThreshhold;

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
//                || queryResponse.getResults().getNumFound() > spellCheckNumfoundThreshhold
                || searchServiceRequest == null
                || searchServiceRequest.getQ() == null) {
            return searchServiceResponse;
        }

        List<DidYouMean> didYouMeanList ;
        if(searchServiceRequest.isFuzzyCompare()) {
            didYouMeanList = getDidYouMeansFromJLanguageTools(searchServiceResponse, searchServiceRequest.getQ());
        } else {
            didYouMeanList = getDidYouMeansFromSolr(queryResponse, searchServiceResponse, searchServiceRequest.getQ());
        }
        searchServiceResponse.setDidYouMeanList(didYouMeanList);
        return searchServiceResponse;
    }



    @NotNull
    private List<DidYouMean> getDidYouMeansFromJLanguageTools(SearchServiceResponse searchServiceResponse, String term) {
        String originalQuery = GlobalConstants.Q_PREFIX  + term;
        List<RuleMatch> ruleMatchList = spellCorrectUtil.getRuleMatchList(term);
        List<DidYouMean> didYouMeanList = new ArrayList<>();
        for (RuleMatch ruleMatch : ruleMatchList) {
            List<String> suggestedReplacements = ruleMatch.getSuggestedReplacements();
            for(String suggestion: suggestedReplacements) {
                int fromIndex = ruleMatch.getFromPos() ;
                int toIndex = ruleMatch.getToPos() ;
                int endIndex = toIndex;
                while(endIndex < term.length() ) {
                    if(term.charAt(endIndex) == ' ') {
                        break;
                    }
                    endIndex++;
                }
                String finalSuggestion = term.substring(0, fromIndex)
                        + GlobalConstants.SPACE
                        +  suggestion
                        + term.substring(endIndex, term.length()).trim();

                DidYouMean didYouMean = new DidYouMean();
                didYouMean.setSuggestedTerm(finalSuggestion);
                didYouMean.setUrl(searchServiceResponse.getOriginalQuery().replace(originalQuery,  GlobalConstants.Q_PREFIX  + finalSuggestion));
                didYouMeanList.add(didYouMean);
            }
        }
        return didYouMeanList;
    }


    public  List<DidYouMean> getDidYouMeansFromSolr(QueryResponse queryResponse, SearchServiceResponse searchServiceResponse, String term) {
        String originalQuery = GlobalConstants.Q_PREFIX  + term;
        SpellCheckResponse spellCheckResponse = queryResponse.getSpellCheckResponse();
        List<DidYouMean> didYouMeanList = new ArrayList<>();
        if(spellCheckResponse == null) {
            return didYouMeanList;
        }
        if (!term.contains(GlobalConstants.SPACE)
                || spellCheckResponse.getCollatedResults() == null
                || spellCheckResponse.getCollatedResults().size() == 0) {
            List<SpellCheckResponse.Suggestion> suggestions = spellCheckResponse.getSuggestions();
            if (suggestions != null) {
                for (SpellCheckResponse.Suggestion suggestion : suggestions) {
                    List<Integer> alternativeFrequencies = suggestion.getAlternativeFrequencies();
                    List<String> alternatives = suggestion.getAlternatives();
                    if (alternatives != null && alternativeFrequencies != null && alternatives.size() == alternativeFrequencies.size()) {
                        int count = 0;
                        for (String alternative : alternatives) {
                            if (!alternative.contains(GlobalConstants.SPACE)) {
                                LOGGER.info("Spell check alternative " + alternative);
                                DidYouMean didYouMean = new DidYouMean();
                                didYouMean.setSuggestedTerm(alternative);
                                didYouMean.setNumberOfResults(alternativeFrequencies.get(count));

                                didYouMean.setUrl(searchServiceResponse.getOriginalQuery().replace(originalQuery,  GlobalConstants.Q_PREFIX + alternative));
                                didYouMeanList.add(didYouMean);
                            }
                            count++;
                        }
                    }
                }
            }
        } else {
            List<SpellCheckResponse.Collation> collations = spellCheckResponse.getCollatedResults();
            if (collations != null) {
                for (SpellCheckResponse.Collation collation : collations) {
                    LOGGER.info("Spell check collation " + collation.getCollationQueryString());
                    DidYouMean didYouMean = new DidYouMean();
                    didYouMean.setSuggestedTerm(collation.getCollationQueryString());
                    didYouMean.setNumberOfResults(collation.getNumberOfHits());
                    didYouMean.setUrl(searchServiceResponse.getOriginalQuery().replace(originalQuery,   GlobalConstants.Q_PREFIX + collation.getCollationQueryString()));
                    didYouMeanList.add(didYouMean);
                }
            }
        }
        return didYouMeanList;
    }

}