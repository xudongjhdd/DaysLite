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

    private final SharedPreferences preferences;

    CountdownStore(Context context) {
        preferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    List<CountdownEvent> loadEvents() {
        List<CountdownEvent> events = new ArrayList<>();
        String raw = preferences.getString(KEY_EVENTS, "[]");
        try {
            JSONArray array = new JSONArray(raw);
            for (int i = 0; i < array.length(); i++) {
                JSONObject item = array.getJSONObject(i);
                events.add(CountdownEvent.fromJson(item));
            }
        } catch (JSONException ignored) {
            events.clear();
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
}
