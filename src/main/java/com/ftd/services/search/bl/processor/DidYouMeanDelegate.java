package com.ftd.services.search.bl.processor;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.jetbrains.annotations.NotNull;
import org.languagetool.rules.RuleMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ftd.services.search.api.request.SearchServiceRequest;
import com.ftd.services.search.api.response.DidYouMean;
import com.ftd.services.search.api.response.SearchServiceResponse;
import com.ftd.services.search.config.GlobalConstants;
import com.ftd.services.search.util.SpellCorrectUtil;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named("didYouMeanDelegate")
public class DidYouMeanDelegate extends BaseDelegate {
    private static final Logger LOGGER = LoggerFactory.getLogger(DidYouMeanDelegate.class);

//    @Value("${service.searchEndpoint}")
//    private String searchEndpoint;

    private SpellCorrectUtil spellCorrectUtil;

    private LevenshteinDistance levenshteinDistance;

    @Autowired
    public void setSpellCorrectUtil(SpellCorrectUtil spellCorrectUtil) {
        this.spellCorrectUtil = spellCorrectUtil;
    }

    @PostConstruct
    public void setLevenshteinDistance() {
        this.levenshteinDistance = LevenshteinDistance.getDefaultInstance();
    }

    @Override
    public SolrQuery preProcessQuery(SolrQuery solrQuery, SearchServiceRequest searchServiceRequest) {
        return solrQuery;
    }

    @Override
    public SearchServiceResponse postProcessResult(SearchServiceRequest searchServiceRequest,
                                                   QueryResponse queryResponse,
                                                   SearchServiceResponse searchServiceResponse) {

        if (queryResponse == null
                || queryResponse.getResults() == null
//                || queryResponse.getResults().getNumFound() > spellCheckNumfoundThreshhold
                || searchServiceRequest == null
                || searchServiceRequest.getQ() == null) {
            return searchServiceResponse;
        }

        List<DidYouMean> didYouMeanList;
        if (searchServiceRequest.isFuzzyCompare()) {
            didYouMeanList = getDidYouMeansFromJLanguageTools(searchServiceResponse, searchServiceRequest.getQ());
        } else {
            didYouMeanList = getDidYouMeansFromSolr(
                    queryResponse,
                    searchServiceResponse,
                    searchServiceRequest.getQ());
        }
        searchServiceResponse.setDidYouMeanList(didYouMeanList);
        return searchServiceResponse;
    }


    @NotNull
    private List<DidYouMean> getDidYouMeansFromJLanguageTools(
            SearchServiceResponse searchServiceResponse,
            String term) {
        String originalQuery = GlobalConstants.Q_PREFIX + term;
        List<RuleMatch> ruleMatchList = spellCorrectUtil.getRuleMatchList(term);
        List<DidYouMean> didYouMeanList = new ArrayList<>();
        for (RuleMatch ruleMatch : ruleMatchList) {
            List<String> suggestedReplacements = ruleMatch.getSuggestedReplacements();
            for (String suggestion : suggestedReplacements) {
                int fromIndex = ruleMatch.getFromPos();
                int endIndex = ruleMatch.getToPos();
                if (!suggestion.startsWith(term.substring(fromIndex, fromIndex + 1))) {
                    continue;
                }
                while (endIndex < term.length()) {
                    if (term.charAt(endIndex) == ' ') {
                        break;
                    }
                    endIndex++;
                }
                String finalSuggestion = (term.substring(0, fromIndex).trim()
                        + GlobalConstants.SPACE
                        + suggestion
                        + term.substring(endIndex, term.length()).trim()).trim();
                if (levenshteinDistance.apply(finalSuggestion, term) > 1
                        || !finalSuggestion.startsWith(term.substring(0, 1))) {
                    continue;
                }
                DidYouMean didYouMean = new DidYouMean();
                didYouMean.setSuggestedTerm(finalSuggestion);
                didYouMean.setUrl(
//                        searchEndpoint +
                        GlobalConstants.QUESTION_MARK +
                                searchServiceResponse.getOriginalQuery().replace(
                                        originalQuery,
                                        GlobalConstants.Q_PREFIX + finalSuggestion));
                didYouMeanList.add(didYouMean);
            }
        }
        return didYouMeanList;
    }


    public List<DidYouMean> getDidYouMeansFromSolr(
            QueryResponse queryResponse,
            SearchServiceResponse searchServiceResponse,
            String term) {
        String originalQuery = GlobalConstants.Q_PREFIX + term;
        SpellCheckResponse spellCheckResponse = queryResponse.getSpellCheckResponse();
        List<DidYouMean> didYouMeanList = new ArrayList<>();
        if (spellCheckResponse == null) {
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
                    if (alternatives != null
                            && alternativeFrequencies != null
                            && alternatives.size() == alternativeFrequencies.size()) {
                        int count = 0;
                        for (String alternative : alternatives) {
                            if (!alternative.contains(GlobalConstants.SPACE)) {
                                LOGGER.info("Spell check alternative " + alternative);
                                DidYouMean didYouMean = new DidYouMean();
                                didYouMean.setSuggestedTerm(alternative);
                                didYouMean.setNumberOfResults(alternativeFrequencies.get(count));

                                didYouMean.setUrl(
//                                        searchEndpoint +
                                        GlobalConstants.QUESTION_MARK +
                                                searchServiceResponse.getOriginalQuery().replace(
                                                        originalQuery,
                                                        GlobalConstants.Q_PREFIX + alternative));
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
                    didYouMean.setUrl(
//                            searchEndpoint +
                            GlobalConstants.QUESTION_MARK +
                                    searchServiceResponse.getOriginalQuery().replace(
                                            originalQuery,
                                            GlobalConstants.Q_PREFIX + collation.getCollationQueryString()));
                    didYouMeanList.add(didYouMean);
                }
            }
        }
        return didYouMeanList;
    }

}