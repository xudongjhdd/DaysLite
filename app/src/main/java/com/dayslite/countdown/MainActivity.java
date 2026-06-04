package com.dayslite.countdown;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class MainActivity extends Activity {
    private final CountdownCalculator calculator = new CountdownCalculator();
    private final List<CountdownEvent> events = new ArrayList<>();

    private CountdownStore store;
    private UiKit ui;
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
        store = new CountdownStore(this);
        ui = new UiKit(this);
        events.addAll(store.loadEvents());
        showHome();
    }

    private void showHome() {
        currentScreen = Screen.HOME;
        editingEvent = null;
        root = ui.vertical();
        root.setBackgroundColor(Color.parseColor("#F8FAFC"));
        root.setPadding(ui.dp(20), ui.dp(20), ui.dp(20), ui.dp(20));

        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(root);
        setContentView(scrollView);

        LinearLayout header = ui.horizontal();
        header.setGravity(Gravity.CENTER_VERTICAL);
        root.addView(header, ui.matchWrap());

        LinearLayout titles = ui.vertical();
        header.addView(titles, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        titles.addView(ui.text("DaysLite", 30, "#0F172A", Typeface.BOLD));
        titles.addView(ui.text(calculator.summaryText(events), 15, "#64748B", Typeface.NORMAL));

        TextView settings = ui.pill("Settings", "#E2E8F0", "#0F172A");
        settings.setOnClickListener(v -> showSettings());
        header.addView(settings);

        ui.addSpace(root, 22);

        if (events.isEmpty()) {
            root.addView(emptyState(), ui.matchWrap());
        } else {
            List<CountdownEvent> sorted = new ArrayList<>(events);
            Collections.sort(sorted, Comparator.comparing(event -> event.targetDate));
            for (CountdownEvent event : sorted) {
                root.addView(card(event), ui.matchWrap());
                ui.addSpace(root, 12);
            }

            ui.addSpace(root, 10);
            Button add = ui.primaryButton("+ Add countdown");
            add.setOnClickListener(v -> showEditor(null));
            root.addView(add, ui.matchWrap());
        }
    }

    private View emptyState() {
        LinearLayout box = ui.vertical();
        box.setGravity(Gravity.CENTER_HORIZONTAL);
        box.setPadding(ui.dp(20), ui.dp(34), ui.dp(20), ui.dp(34));
        box.setBackground(ui.round("#FFFFFF", 18, "#E2E8F0"));

        TextView mark = ui.text("7", 44, "#2563EB", Typeface.BOLD);
        mark.setGravity(Gravity.CENTER);
        box.addView(mark);
        box.addView(ui.text("No countdowns yet", 22, "#0F172A", Typeface.BOLD));
        TextView helper = ui.text("Add a date worth looking forward to.", 15, "#64748B", Typeface.NORMAL);
        helper.setGravity(Gravity.CENTER);
        box.addView(helper);
        ui.addSpace(box, 18);

        Button add = ui.primaryButton("Add countdown");
        add.setOnClickListener(v -> showEditor(null));
        box.addView(add, ui.matchWrap());
        return box;
    }

    private View card(CountdownEvent event) {
        LinearLayout card = ui.vertical();
        card.setPadding(ui.dp(18), ui.dp(16), ui.dp(18), ui.dp(16));
        card.setBackground(ui.round("#FFFFFF", 16, "#E2E8F0"));
        card.setOnClickListener(v -> showEditor(event));

        LinearLayout top = ui.horizontal();
        top.setGravity(Gravity.CENTER_VERTICAL);
        card.addView(top, ui.matchWrap());

        TextView title = ui.text(event.title, 20, "#0F172A", Typeface.BOLD);
        top.addView(title, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        TextView dot = new TextView(this);
        dot.setWidth(ui.dp(16));
        dot.setHeight(ui.dp(16));
        dot.setBackground(ui.round(event.color, 99, event.color));
        top.addView(dot);

        ui.addSpace(card, 10);

        TextView days = ui.text(calculator.daysLabel(event.targetDate), 34, event.color, Typeface.BOLD);
        card.addView(days);
        card.addView(ui.text(event.targetDate.format(CountdownCalculator.DISPLAY_DATE), 15, "#64748B", Typeface.NORMAL));

        if (!event.note.trim().isEmpty()) {
            ui.addSpace(card, 8);
            card.addView(ui.text(event.note, 14, "#334155", Typeface.NORMAL));
        }
        return card;
    }

    private void showEditor(CountdownEvent event) {
        currentScreen = Screen.EDITOR;
        editingEvent = event;
        selectedDate = event == null ? LocalDate.now() : event.targetDate;
        selectedColor = event == null ? "#2563EB" : event.color;

        root = ui.vertical();
        root.setBackgroundColor(Color.parseColor("#F8FAFC"));
        root.setPadding(ui.dp(20), ui.dp(20), ui.dp(20), ui.dp(20));
        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(root);
        setContentView(scrollView);

        addTopBar(event == null ? "Add countdown" : "Edit countdown", v -> showHome());

        addLabel("Event name");
        titleInput = ui.input("Trip, birthday, exam...");
        titleInput.setText(event == null ? "" : event.title);
        root.addView(titleInput, ui.matchWrap());

        ui.addSpace(root, 16);
        addLabel("Target date");
        dateValue = ui.pill(selectedDate.format(CountdownCalculator.DISPLAY_DATE), "#FFFFFF", "#0F172A");
        dateValue.setGravity(Gravity.CENTER_VERTICAL);
        dateValue.setPadding(ui.dp(16), ui.dp(14), ui.dp(16), ui.dp(14));
        dateValue.setOnClickListener(v -> pickDate());
        root.addView(dateValue, ui.matchWrap());

        ui.addSpace(root, 16);
        addLabel("Color");
        colorRow = ui.horizontal();
        colorRow.setGravity(Gravity.CENTER_VERTICAL);
        root.addView(colorRow, ui.matchWrap());
        renderColors();

        ui.addSpace(root, 16);
        addLabel("Note");
        noteInput = ui.input("Optional");
        noteInput.setText(event == null ? "" : event.note);
        noteInput.setMinLines(3);
        noteInput.setGravity(Gravity.TOP);
        root.addView(noteInput, ui.matchWrap());

        ui.addSpace(root, 24);
        Button save = ui.primaryButton("Save");
        save.setOnClickListener(v -> saveEditor());
        root.addView(save, ui.matchWrap());

        if (event != null) {
            ui.addSpace(root, 12);
            Button delete = ui.secondaryButton("Delete");
            delete.setTextColor(Color.parseColor("#DC2626"));
            delete.setOnClickListener(v -> confirmDelete(event));
            root.addView(delete, ui.matchWrap());
        }
    }

    private void showSettings() {
        currentScreen = Screen.SETTINGS;
        root = ui.vertical();
        root.setBackgroundColor(Color.parseColor("#F8FAFC"));
        root.setPadding(ui.dp(20), ui.dp(20), ui.dp(20), ui.dp(20));
        setContentView(root);

        addTopBar("Settings", v -> showHome());
        root.addView(settingsItem("Privacy Policy", "How DaysLite handles data", v -> showPrivacyPolicy()), ui.matchWrap());
        ui.addSpace(root, 12);
        root.addView(settingsItem("Local storage", "Countdowns are saved on this device only", null), ui.matchWrap());
        ui.addSpace(root, 12);
        root.addView(settingsItem("Version", "1.0.0", null), ui.matchWrap());
    }

    private void showPrivacyPolicy() {
        currentScreen = Screen.PRIVACY;
        root = ui.vertical();
        root.setBackgroundColor(Color.parseColor("#F8FAFC"));
        root.setPadding(ui.dp(20), ui.dp(20), ui.dp(20), ui.dp(20));
        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(root);
        setContentView(scrollView);

        addTopBar("Privacy Policy", v -> showSettings());
        root.addView(ui.text("DaysLite does not collect, transmit, sell, or share personal data.", 18, "#0F172A", Typeface.BOLD));
        ui.addSpace(root, 12);
        root.addView(ui.text("Countdown events, dates, colors, and notes are stored locally on your device. They are not sent to DaysLite servers or shared with third parties.", 15, "#334155", Typeface.NORMAL));
        ui.addSpace(root, 12);
        root.addView(ui.text("DaysLite does not require account login, location access, contacts, photos, microphone, camera, or other sensitive permissions.", 15, "#334155", Typeface.NORMAL));
        ui.addSpace(root, 12);
        root.addView(ui.text("You can delete individual countdowns inside the app. You can also delete all app data by uninstalling DaysLite or clearing app storage from Android system settings.", 15, "#334155", Typeface.NORMAL));
        ui.addSpace(root, 12);
        root.addView(ui.text("For support, use the GitHub repository contact path provided by the developer.", 15, "#64748B", Typeface.NORMAL));
    }

    private View settingsItem(String title, String detail, View.OnClickListener listener) {
        LinearLayout item = ui.vertical();
        item.setPadding(ui.dp(16), ui.dp(14), ui.dp(16), ui.dp(14));
        item.setBackground(ui.round("#FFFFFF", 16, "#E2E8F0"));
        item.addView(ui.text(title, 18, "#0F172A", Typeface.BOLD));
        item.addView(ui.text(detail, 14, "#64748B", Typeface.NORMAL));
        if (listener != null) {
            item.setOnClickListener(listener);
        }
        return item;
    }

    private void addTopBar(String title, View.OnClickListener backAction) {
        LinearLayout bar = ui.horizontal();
        bar.setGravity(Gravity.CENTER_VERTICAL);
        root.addView(bar, ui.matchWrap());

        TextView back = ui.pill("Back", "#E2E8F0", "#0F172A");
        back.setOnClickListener(backAction);
        bar.addView(back);

        TextView heading = ui.text(title, 24, "#0F172A", Typeface.BOLD);
        heading.setGravity(Gravity.RIGHT);
        bar.addView(heading, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        ui.addSpace(root, 24);
    }

    private void pickDate() {
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate = LocalDate.of(year, month + 1, dayOfMonth);
                    dateValue.setText(selectedDate.format(CountdownCalculator.DISPLAY_DATE));
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
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ui.dp(38), ui.dp(38));
            params.setMargins(0, 0, ui.dp(10), 0);
            swatch.setLayoutParams(params);
            swatch.setBackground(ui.round(color, 99, selectedColor.equals(color) ? "#0F172A" : color, selectedColor.equals(color) ? 3 : 0));
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
        store.saveEvents(events);
        showHome();
    }

    private void confirmDelete(CountdownEvent event) {
        new AlertDialog.Builder(this)
                .setTitle("Delete countdown?")
                .setMessage("This removes the countdown from this device.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    events.remove(event);
                    store.saveEvents(events);
                    showHome();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void addLabel(String value) {
        TextView label = ui.text(value, 14, "#64748B", Typeface.BOLD);
        root.addView(label);
        ui.addSpace(root, 8);
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
}
