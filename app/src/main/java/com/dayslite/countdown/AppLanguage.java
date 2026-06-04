package com.dayslite.countdown;

enum AppLanguage {
    ENGLISH("en"),
    CHINESE("zh");

    final String code;

    AppLanguage(String code) {
        this.code = code;
    }

    static AppLanguage fromCode(String code) {
        if (CHINESE.code.equals(code)) {
            return CHINESE;
        }
        return ENGLISH;
    }

    AppLanguage toggled() {
        return this == ENGLISH ? CHINESE : ENGLISH;
    }
}
