package com.micro.services.search.util;

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
    //    public static final String MORFOLOGIK_RULE_EN_US = "MORFOLOGIK_RULE_EN_US";
    public static final String UPPERCASE_SENTENCE_START = "UPPERCASE_SENTENCE_START";

    public JLanguageTool getNewLanguageTool() {
        final JLanguageTool languageTool = new JLanguageTool(LanguageInitializer.INSTANCE.getLanguage());
        languageTool.disableRule(UPPERCASE_SENTENCE_START);
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

}
