package com.dayslite.countdown;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class MainActivity extends Activity {
    private static final String PREFS = "dayslite";
    private static final String KEY_EVENTS = "events";
    private static final DateTimeFormatter STORE_DATE = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter DISPLAY_DATE = DateTimeFormatter.ofPattern("MMM d, yyyy");

    private final List<CountdownEvent> events = new ArrayList<>();
    private Screen currentScreen = Screen.HOME;
    private LinearLayout root;
    private CountdownEvent editingEvent;
    private LocalDate selectedDate;
    private String selectedColor = "#2563EB";
    private EditText titleInput;
    private EditText noteInput;
    private TextView dateValue;
    private LinearLayout colorRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadEvents();
        showHome();
    }

    private void showHome() {
        currentScreen = Screen.HOME;
        editingEvent = null;
        root = vertical();
        root.setBackgroundColor(Color.parseColor("#F8FAFC"));
        root.setPadding(dp(20), dp(20), dp(20), dp(20));

        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(root);
        setContentView(scrollView);

        LinearLayout header = horizontal();
        header.setGravity(Gravity.CENTER_VERTICAL);
        root.addView(header, matchWrap());

        LinearLayout titles = vertical();
        header.addView(titles, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        titles.addView(text("DaysLite", 30, "#0F172A", Typeface.BOLD));
        titles.addView(text(summaryText(), 15, "#64748B", Typeface.NORMAL));

        TextView settings = pill("Settings", "#E2E8F0", "#0F172A");
        settings.setOnClickListener(v -> showSettings());
        header.addView(settings);

        addSpace(root, 22);

        if (events.isEmpty()) {
            root.addView(emptyState(), matchWrap());
        } else {
            List<CountdownEvent> sorted = new ArrayList<>(events);
            Collections.sort(sorted, Comparator.comparing(event -> event.targetDate));
            for (CountdownEvent event : sorted) {
                root.addView(card(event), matchWrap());
                addSpace(root, 12);
            }
        }

        addSpace(root, 10);
        Button add = primaryButton("+ Add countdown");
        add.setOnClickListener(v -> showEditor(null));
        root.addView(add, matchWrap());
    }

    private View emptyState() {
        LinearLayout box = vertical();
        box.setGravity(Gravity.CENTER_HORIZONTAL);
        box.setPadding(dp(20), dp(34), dp(20), dp(34));
        box.setBackground(round("#FFFFFF", 18, "#E2E8F0"));

        TextView mark = text("7", 44, "#2563EB", Typeface.BOLD);
        mark.setGravity(Gravity.CENTER);
        box.addView(mark);
        box.addView(text("No countdowns yet", 22, "#0F172A", Typeface.BOLD));
        TextView helper = text("Add a date worth looking forward to.", 15, "#64748B", Typeface.NORMAL);
        helper.setGravity(Gravity.CENTER);
        box.addView(helper);
        addSpace(box, 18);

        Button add = primaryButton("Add countdown");
        add.setOnClickListener(v -> showEditor(null));
        box.addView(add, matchWrap());
        return box;
    }

    private View card(CountdownEvent event) {
        LinearLayout card = vertical();
        card.setPadding(dp(18), dp(16), dp(18), dp(16));
        card.setBackground(round("#FFFFFF", 16, "#E2E8F0"));
        card.setOnClickListener(v -> showEditor(event));

        LinearLayout top = horizontal();
        top.setGravity(Gravity.CENTER_VERTICAL);
        card.addView(top, matchWrap());

        TextView title = text(event.title, 20, "#0F172A", Typeface.BOLD);
        top.addView(title, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        TextView dot = new TextView(this);
        dot.setWidth(dp(16));
        dot.setHeight(dp(16));
        dot.setBackground(round(event.color, 99, event.color));
        top.addView(dot);

        addSpace(card, 10);

        TextView days = text(daysLabel(event.targetDate), 34, event.color, Typeface.BOLD);
        card.addView(days);
        card.addView(text(event.targetDate.format(DISPLAY_DATE), 15, "#64748B", Typeface.NORMAL));

        if (!event.note.trim().isEmpty()) {
            addSpace(card, 8);
            card.addView(text(event.note, 14, "#334155", Typeface.NORMAL));
        }
        return card;
    }

    private void showEditor(CountdownEvent event) {
        currentScreen = Screen.EDITOR;
        editingEvent = event;
        selectedDate = event == null ? LocalDate.now() : event.targetDate;
        selectedColor = event == null ? "#2563EB" : event.color;

        root = vertical();
        root.setBackgroundColor(Color.parseColor("#F8FAFC"));
        root.setPadding(dp(20), dp(20), dp(20), dp(20));
        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(root);
        setContentView(scrollView);

        addTopBar(event == null ? "Add countdown" : "Edit countdown", v -> showHome());

        addLabel("Event name");
        titleInput = input("Trip, birthday, exam...");
        titleInput.setText(event == null ? "" : event.title);
        root.addView(titleInput, matchWrap());

        addSpace(root, 16);
        addLabel("Target date");
        dateValue = pill(selectedDate.format(DISPLAY_DATE), "#FFFFFF", "#0F172A");
        dateValue.setGravity(Gravity.CENTER_VERTICAL);
        dateValue.setPadding(dp(16), dp(14), dp(16), dp(14));
        dateValue.setOnClickListener(v -> pickDate());
        root.addView(dateValue, matchWrap());

        addSpace(root, 16);
        addLabel("Color");
        colorRow = horizontal();
        colorRow.setGravity(Gravity.CENTER_VERTICAL);
        root.addView(colorRow, matchWrap());
        renderColors();

        addSpace(root, 16);
        addLabel("Note");
        noteInput = input("Optional");
        noteInput.setText(event == null ? "" : event.note);
        noteInput.setMinLines(3);
        noteInput.setGravity(Gravity.TOP);
        root.addView(noteInput, matchWrap());

        addSpace(root, 24);
        Button save = primaryButton("Save");
        save.setOnClickListener(v -> saveEditor());
        root.addView(save, matchWrap());

        if (event != null) {
            addSpace(root, 12);
            Button delete = secondaryButton("Delete");
            delete.setTextColor(Color.parseColor("#DC2626"));
            delete.setOnClickListener(v -> confirmDelete(event));
            root.addView(delete, matchWrap());
        }
    }

    private void showSettings() {
        currentScreen = Screen.SETTINGS;
        root = vertical();
        root.setBackgroundColor(Color.parseColor("#F8FAFC"));
        root.setPadding(dp(20), dp(20), dp(20), dp(20));
        setContentView(root);

        addTopBar("Settings", v -> showHome());
        root.addView(settingsItem("Privacy Policy", "How DaysLite handles data", v -> showPrivacyPolicy()), matchWrap());
        addSpace(root, 12);
        root.addView(settingsItem("Local storage", "Countdowns are saved on this device only", null), matchWrap());
        addSpace(root, 12);
        root.addView(settingsItem("Version", "1.0.0", null), matchWrap());
    }

    private void showPrivacyPolicy() {
        currentScreen = Screen.PRIVACY;
        root = vertical();
        root.setBackgroundColor(Color.parseColor("#F8FAFC"));
        root.setPadding(dp(20), dp(20), dp(20), dp(20));
        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(root);
        setContentView(scrollView);

        addTopBar("Privacy Policy", v -> showSettings());
        root.addView(text("DaysLite does not collect, transmit, sell, or share personal data.", 18, "#0F172A", Typeface.BOLD));
        addSpace(root, 12);
        root.addView(text("Countdown events, dates, colors, and notes are stored locally on your device. They are not sent to DaysLite servers or shared with third parties.", 15, "#334155", Typeface.NORMAL));
        addSpace(root, 12);
        root.addView(text("DaysLite does not require account login, location access, contacts, photos, microphone, camera, or other sensitive permissions.", 15, "#334155", Typeface.NORMAL));
        addSpace(root, 12);
        root.addView(text("You can delete individual countdowns inside the app. You can also delete all app data by uninstalling DaysLite or clearing app storage from Android system settings.", 15, "#334155", Typeface.NORMAL));
        addSpace(root, 12);
        root.addView(text("For support, use the GitHub repository contact path provided by the developer.", 15, "#64748B", Typeface.NORMAL));
    }

    private View settingsItem(String title, String detail, View.OnClickListener listener) {
        LinearLayout item = vertical();
        item.setPadding(dp(16), dp(14), dp(16), dp(14));
        item.setBackground(round("#FFFFFF", 16, "#E2E8F0"));
        item.addView(text(title, 18, "#0F172A", Typeface.BOLD));
        item.addView(text(detail, 14, "#64748B", Typeface.NORMAL));
        if (listener != null) {
            item.setOnClickListener(listener);
        }
        return item;
    }

    private void addTopBar(String title, View.OnClickListener backAction) {
        LinearLayout bar = horizontal();
        bar.setGravity(Gravity.CENTER_VERTICAL);
        root.addView(bar, matchWrap());

        TextView back = pill("Back", "#E2E8F0", "#0F172A");
        back.setOnClickListener(backAction);
        bar.addView(back);

        TextView heading = text(title, 24, "#0F172A", Typeface.BOLD);
        heading.setGravity(Gravity.RIGHT);
        bar.addView(heading, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        addSpace(root, 24);
    }

    private void pickDate() {
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate = LocalDate.of(year, month + 1, dayOfMonth);
                    dateValue.setText(selectedDate.format(DISPLAY_DATE));
                },
                selectedDate.getYear(),
                selectedDate.getMonthValue() - 1,
                selectedDate.getDayOfMonth()
        );
        dialog.show();
    }

    private void renderColors() {
        colorRow.removeAllViews();
        String[] colors = {"#2563EB", "#10B981", "#F97316", "#EC4899", "#7C3AED", "#475569"};
        for (String color : colors) {
            TextView swatch = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp(38), dp(38));
            params.setMargins(0, 0, dp(10), 0);
            swatch.setLayoutParams(params);
            swatch.setBackground(round(color, 99, selectedColor.equals(color) ? "#0F172A" : color, selectedColor.equals(color) ? 3 : 0));
            swatch.setOnClickListener(v -> {
                selectedColor = color;
                renderColors();
            });
            colorRow.addView(swatch);
        }
    }

    private void saveEditor() {
        String title = titleInput.getText().toString().trim();
        String note = noteInput.getText().toString().trim();
        if (title.isEmpty()) {
            titleInput.setError("Name required");
            titleInput.requestFocus();
            InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.showSoftInput(titleInput, InputMethodManager.SHOW_IMPLICIT);
            }
            return;
        }

        long now = System.currentTimeMillis();
        if (editingEvent == null) {
            events.add(new CountdownEvent(UUID.randomUUID().toString(), title, selectedDate, note, selectedColor, now, now));
        } else {
            editingEvent.title = title;
            editingEvent.targetDate = selectedDate;
            editingEvent.note = note;
            editingEvent.color = selectedColor;
            editingEvent.updatedAt = now;
        }
        saveEvents();
        showHome();
    }

    private void confirmDelete(CountdownEvent event) {
        new AlertDialog.Builder(this)
                .setTitle("Delete countdown?")
                .setMessage("This removes the countdown from this device.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    events.remove(event);
                    saveEvents();
                    showHome();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private String daysLabel(LocalDate targetDate) {
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

    private String summaryText() {
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

    private void loadEvents() {
        events.clear();
        SharedPreferences preferences = getSharedPreferences(PREFS, MODE_PRIVATE);
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
    }

    private void saveEvents() {
        JSONArray array = new JSONArray();
        for (CountdownEvent event : events) {
            array.put(event.toJson());
        }
        getSharedPreferences(PREFS, MODE_PRIVATE)
                .edit()
                .putString(KEY_EVENTS, array.toString())
                .apply();
    }

    private void addLabel(String value) {
        TextView label = text(value, 14, "#64748B", Typeface.BOLD);
        root.addView(label);
        addSpace(root, 8);
    }

    private EditText input(String hint) {
        EditText input = new EditText(this);
        input.setHint(hint);
        input.setTextColor(Color.parseColor("#0F172A"));
        input.setHintTextColor(Color.parseColor("#94A3B8"));
        input.setTextSize(16);
        input.setSingleLine(false);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        input.setPadding(dp(16), dp(12), dp(16), dp(12));
        input.setBackground(round("#FFFFFF", 14, "#CBD5E1"));
        return input;
    }

    private Button primaryButton(String value) {
        Button button = new Button(this);
        button.setText(value);
        button.setAllCaps(false);
        button.setTextColor(Color.WHITE);
        button.setTextSize(16);
        button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        button.setPadding(dp(14), dp(10), dp(14), dp(10));
        button.setBackground(round("#2563EB", 14, "#2563EB"));
        return button;
    }

    private Button secondaryButton(String value) {
        Button button = new Button(this);
        button.setText(value);
        button.setAllCaps(false);
        button.setTextSize(16);
        button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        button.setPadding(dp(14), dp(10), dp(14), dp(10));
        button.setBackground(round("#FFFFFF", 14, "#CBD5E1"));
        return button;
    }

    private TextView pill(String value, String background, String foreground) {
        TextView view = text(value, 14, foreground, Typeface.BOLD);
        view.setPadding(dp(14), dp(9), dp(14), dp(9));
        view.setBackground(round(background, 99, "#CBD5E1"));
        return view;
    }

    private TextView text(String value, int sp, String color, int style) {
        TextView text = new TextView(this);
        text.setText(value);
        text.setTextSize(sp);
        text.setTextColor(Color.parseColor(color));
        text.setTypeface(Typeface.DEFAULT, style);
        text.setLineSpacing(dp(2), 1);
        return text;
    }

    private LinearLayout vertical() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        return layout;
    }

    private LinearLayout horizontal() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        return layout;
    }

    private LinearLayout.LayoutParams matchWrap() {
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void addSpace(LinearLayout parent, int dp) {
        View spacer = new View(this);
        parent.addView(spacer, new LinearLayout.LayoutParams(1, dp(dp)));
    }

    private GradientDrawable round(String fill, int radius, String stroke) {
        return round(fill, radius, stroke, 1);
    }

    private GradientDrawable round(String fill, int radius, String stroke, int strokeWidth) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.parseColor(fill));
        drawable.setCornerRadius(dp(radius));
        if (strokeWidth > 0) {
            drawable.setStroke(dp(strokeWidth), Color.parseColor(stroke));
        }
        return drawable;
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    @Override
    public void onBackPressed() {
        if (currentScreen == Screen.HOME) {
            finish();
            return;
        }
        if (currentScreen == Screen.PRIVACY) {
            showSettings();
            return;
        }
        showHome();
    }

    private enum Screen {
        HOME,
        EDITOR,
        SETTINGS,
        PRIVACY
    }

    private static class CountdownEvent {
        String id;
        String title;
        LocalDate targetDate;
        String note;
        String color;
        long createdAt;
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
}
