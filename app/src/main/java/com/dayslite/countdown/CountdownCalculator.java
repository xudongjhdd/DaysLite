package com.dayslite.countdown;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

class CountdownCalculator {
    static final DateTimeFormatter DISPLAY_DATE = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.US);

    String daysLabel(LocalDate targetDate) {
        long days = ChronoUnit.DAYS.between(LocalDate.now(), targetDate);
        if (days == 0) {
            return "Today";
        }
        if (days > 0) {
            return days + (days == 1 ? " day left" : " days left");
        }
        long past = Math.abs(days);
        return past + (past == 1 ? " day ago" : " days ago");
    }

    String summaryText(List<CountdownEvent> events) {
        if (events.isEmpty()) {
            return "A simple countdown app for important days";
        }
        int upcoming = 0;
        LocalDate today = LocalDate.now();
        for (CountdownEvent event : events) {
            if (!event.targetDate.isBefore(today)) {
                upcoming++;
            }
        }
        return upcoming + " upcoming " + (upcoming == 1 ? "day" : "days");
    }
}
