package com.micro.services.search.util;

import com.micro.services.search.config.GlobalConstants;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.languagetool.JLanguageTool;
import org.languagetool.rules.RuleMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class SpellCorrectUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpellCorrectUtil.class);
    public static final String MORFOLOGIK_RULE_EN_US = "MORFOLOGIK_RULE_EN_US";
    public static final String UPPERCASE_SENTENCE_START = "UPPERCASE_SENTENCE_START";

    public JLanguageTool getNewLanguageTool(){
        final JLanguageTool languageTool = new JLanguageTool(LanguageInitializer.INSTANCE.getLanguage());
        languageTool.disableRule(UPPERCASE_SENTENCE_START   );
//        languageTool.disableRule(MORFOLOGIK_RULE_EN_US);
        return languageTool;
    }

    public List<RuleMatch> getRuleMatchList(JLanguageTool languageTool, String term) {
        List<RuleMatch> ruleMatchList = null;
        try {
            ruleMatchList = languageTool.check(term);
        } catch (IOException exception) {
            LOGGER.error("JLanguage tool exception ", exception);
        }
        return ruleMatchList;
    }

    public List<RuleMatch> getRuleMatchList(String term) {
        return getRuleMatchList(getNewLanguageTool(), term);
    }


    public void spellCheck(SpellCheckResponse spellCheckResponse, String term) {
        if (!term.contains(GlobalConstants.SPACE) || spellCheckResponse.getCollatedResults() == null || spellCheckResponse.getCollatedResults().size() == 0) {
            List<SpellCheckResponse.Suggestion> suggestions = spellCheckResponse.getSuggestions();
            if (suggestions != null) {
                for (SpellCheckResponse.Suggestion suggestion : suggestions) {
                    List<String> alternatives = suggestion.getAlternatives();
                    if (alternatives != null) {
                        for (String alternative : alternatives) {
                            if (!alternative.contains(GlobalConstants.SPACE)) {
                                LOGGER.info("Spell check alternative " + alternative);
                                spellCorrectedServiceRequest.setTerm(alternative);
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
