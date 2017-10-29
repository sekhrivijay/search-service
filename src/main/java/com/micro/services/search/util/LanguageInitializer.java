package com.micro.services.search.util;

import org.languagetool.Language;
import org.languagetool.language.AmericanEnglish;

public enum LanguageInitializer {
    INSTANCE(new AmericanEnglish());
    private Language language;

    LanguageInitializer(Language language) {
        this.language = language;
    }

    public Language getLanguage() {
        return language;
    }
}