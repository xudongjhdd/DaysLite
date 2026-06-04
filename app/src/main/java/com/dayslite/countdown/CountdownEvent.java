package com.dayslite.countdown;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class CountdownEvent {
    private static final DateTimeFormatter STORE_DATE = DateTimeFormatter.ISO_LOCAL_DATE;

    final String id;
    String title;
    LocalDate targetDate;
    String note;
    String color;
    final long createdAt;
    long updatedAt;

    CountdownEvent(String id, String title, LocalDate targetDate, String note, String color, long createdAt, long updatedAt) {
        this.id = id;
        this.title = title;
        this.targetDate = targetDate;
        this.note = note;
        this.color = color;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
            object.put("id", id);
            object.put("title", title);
            object.put("targetDate", targetDate.format(STORE_DATE));
            object.put("note", note);
            object.put("color", color);
            object.put("createdAt", createdAt);
            object.put("updatedAt", updatedAt);
        } catch (JSONException ignored) {
        }
        return object;
    }

    static CountdownEvent fromJson(JSONObject object) throws JSONException {
        return new CountdownEvent(
                object.getString("id"),
                object.getString("title"),
                LocalDate.parse(object.getString("targetDate"), STORE_DATE),
                object.optString("note", ""),
                object.optString("color", "#2563EB"),
                object.optLong("createdAt", System.currentTimeMillis()),
                object.optLong("updatedAt", System.currentTimeMillis())
        );
    }
}
