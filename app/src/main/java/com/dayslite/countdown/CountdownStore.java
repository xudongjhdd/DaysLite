package com.dayslite.countdown;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class CountdownStore {
    private static final String PREFS = "dayslite";
    private static final String KEY_EVENTS = "events";
    private static final String KEY_LANGUAGE = "language";

    private final SharedPreferences preferences;

    CountdownStore(Context context) {
        preferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    List<CountdownEvent> loadEvents() {
        String raw = preferences.getString(KEY_EVENTS, "[]");
        return parseEvents(raw);
    }

    static List<CountdownEvent> parseEvents(String raw) {
        List<CountdownEvent> events = new ArrayList<>();
        JSONArray array;
        try {
            array = new JSONArray(raw);
        } catch (JSONException ignored) {
            return events;
        }
        for (int i = 0; i < array.length(); i++) {
            try {
                events.add(CountdownEvent.fromJson(array.getJSONObject(i)));
            } catch (JSONException ignored) {
                // skip this entry only, keep the rest of the data
            }
        }
        return events;
    }

    void saveEvents(List<CountdownEvent> events) {
        JSONArray array = new JSONArray();
        for (CountdownEvent event : events) {
            array.put(event.toJson());
        }
        preferences.edit()
                .putString(KEY_EVENTS, array.toString())
                .apply();
    }

    AppLanguage loadLanguage() {
        return AppLanguage.fromCode(preferences.getString(KEY_LANGUAGE, AppLanguage.ENGLISH.code));
    }

    void saveLanguage(AppLanguage language) {
        preferences.edit()
                .putString(KEY_LANGUAGE, language.code)
                .apply();
    }
}
