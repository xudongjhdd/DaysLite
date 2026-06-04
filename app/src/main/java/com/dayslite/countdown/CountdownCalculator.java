package com.dayslite.countdown;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

class CountdownCalculator {
    private static final DateTimeFormatter ENGLISH_DATE = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.US);
    private static final DateTimeFormatter CHINESE_DATE = DateTimeFormatter.ofPattern("yyyy年M月d日", Locale.CHINA);

    String formatDate(LocalDate date, AppLanguage language) {
        return date.format(language == AppLanguage.CHINESE ? CHINESE_DATE : ENGLISH_DATE);
    }

    String daysLabel(LocalDate targetDate, AppLanguage language) {
        long days = ChronoUnit.DAYS.between(LocalDate.now(), targetDate);
        if (days == 0) {
            return language == AppLanguage.CHINESE ? "今天" : "Today";
        }
        if (days > 0) {
            if (language == AppLanguage.CHINESE) {
                return "还剩 " + days + " 天";
            }
            return days + (days == 1 ? " day left" : " days left");
        }
        long past = Math.abs(days);
        if (language == AppLanguage.CHINESE) {
            return "已过 " + past + " 天";
        }
        return past + (past == 1 ? " day ago" : " days ago");
    }

    String summaryText(List<CountdownEvent> events, AppLanguage language) {
        if (events.isEmpty()) {
            return language == AppLanguage.CHINESE ? "记录重要日子的轻量倒计时" : "A simple countdown app for important days";
        }
        int upcoming = 0;
        LocalDate today = LocalDate.now();
        for (CountdownEvent event : events) {
            if (!event.targetDate.isBefore(today)) {
                upcoming++;
            }
        }
        if (language == AppLanguage.CHINESE) {
            return upcoming + " 个即将到来的日子";
        }
        return upcoming + " upcoming " + (upcoming == 1 ? "day" : "days");
    }
}
