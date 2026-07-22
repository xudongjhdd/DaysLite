package com.dayslite.countdown;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.window.OnBackInvokedDispatcher;

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
    private AppLanguage language = AppLanguage.ENGLISH;
    private AppText text = new AppText(language);
    private Screen currentScreen = Screen.HOME;
    private LinearLayout root;
    private CountdownEvent editingEvent;
    private LocalDate selectedDate;
    private String selectedColor = "#2563EB";
    private EditText titleInput;
    private EditText noteInput;
    private TextView dateValue;
    private LinearLayout colorRow;
    private CheckBox repeatYearlyInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getOnBackInvokedDispatcher().registerOnBackInvokedCallback(
                    OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                    this::handleBackNavigation);
        }
        UiKit.applyLightNavigationBar(this);
        store = new CountdownStore(this);
        ui = new UiKit(this);
        language = store.loadLanguage();
        text = new AppText(language);
        events.addAll(store.loadEvents());
        showHome();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Language can be changed in SettingsActivity; pick up the new value and re-render
        // the home screen so the summary and labels reflect it after returning.
        AppLanguage stored = store.loadLanguage();
        if (stored != language) {
            language = stored;
            text = new AppText(language);
            if (currentScreen == Screen.HOME) {
                showHome();
            }
        }
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
        titles.addView(ui.text(calculator.summaryText(events, language), 15, "#64748B", Typeface.NORMAL));

        TextView settings = ui.pill(text.settings(), "#E2E8F0", "#0F172A");
        settings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
        header.addView(settings);

        ui.addSpace(root, 22);

        if (events.isEmpty()) {
            root.addView(emptyState(), ui.matchWrap());
        } else {
            List<CountdownEvent> sorted = new ArrayList<>(events);
            Collections.sort(sorted, Comparator.comparing(event -> calculator.displayDate(event)));
            for (CountdownEvent event : sorted) {
                root.addView(card(event), ui.matchWrap());
                ui.addSpace(root, 12);
            }

            ui.addSpace(root, 10);
            Button add = ui.primaryButton(text.addCountdownAction());
            add.setOnClickListener(v -> showEditor(null));
            root.addView(add, ui.matchWrap());
        }
    }

    private View emptyState() {
        LinearLayout box = ui.vertical();
        box.setGravity(Gravity.CENTER_HORIZONTAL);
        box.setPadding(ui.dp(20), ui.dp(34), ui.dp(20), ui.dp(34));
        box.setBackground(ui.round("#FFFFFF", 18, "#E2E8F0"));

        box.addView(ui.text(text.noCountdownsYet(), 22, "#0F172A", Typeface.BOLD));
        TextView helper = ui.text(text.emptyHelper(), 15, "#64748B", Typeface.NORMAL);
        helper.setGravity(Gravity.CENTER);
        box.addView(helper);
        ui.addSpace(box, 18);

        Button add = ui.primaryButton(text.addCountdown());
        add.setOnClickListener(v -> showEditor(null));
        box.addView(add, ui.matchWrap());
        return box;
    }

    private View card(CountdownEvent event) {
        boolean isPast = calculator.isPast(event);
        String cardBackground = isPast ? "#F8FAFC" : "#FFFFFF";
        String cardBorder = isPast ? "#CBD5E1" : "#E2E8F0";
        String titleColor = isPast ? "#64748B" : "#0F172A";
        String accentColor = isPast ? "#94A3B8" : event.color;
        String dateColor = isPast ? "#94A3B8" : "#64748B";
        String noteColor = isPast ? "#64748B" : "#334155";
        String dotColor = isPast ? "#CBD5E1" : event.color;

        LinearLayout card = ui.vertical();
        card.setPadding(ui.dp(18), ui.dp(16), ui.dp(18), ui.dp(16));
        card.setBackground(ui.round(cardBackground, 16, cardBorder));
        card.setOnClickListener(v -> showEditor(event));

        LinearLayout top = ui.horizontal();
        top.setGravity(Gravity.CENTER_VERTICAL);
        card.addView(top, ui.matchWrap());

        TextView title = ui.text(event.title, 20, titleColor, Typeface.BOLD);
        top.addView(title, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        TextView dot = new TextView(this);
        dot.setWidth(ui.dp(16));
        dot.setHeight(ui.dp(16));
        dot.setBackground(ui.round(dotColor, 99, dotColor));
        top.addView(dot);

        ui.addSpace(card, 10);

        TextView days = ui.text(calculator.daysLabel(event, language), 34, accentColor, Typeface.BOLD);
        card.addView(days);
        card.addView(ui.text(calculator.formatDate(calculator.displayDate(event), language), 15, dateColor, Typeface.NORMAL));

        if (event.repeatYearly) {
            ui.addSpace(card, 8);
            TextView yearly = ui.pill(text.yearlyBadge(), "#EFF6FF", event.color);
            card.addView(yearly);
        }

        if (!event.note.trim().isEmpty()) {
            ui.addSpace(card, 8);
            card.addView(ui.text(event.note, 14, noteColor, Typeface.NORMAL));
        }
        return card;
    }

    private void showEditor(CountdownEvent event) {
        currentScreen = Screen.EDITOR;
        editingEvent = event;
        selectedDate = event == null ? LocalDate.now().plusDays(1) : event.targetDate;
        selectedColor = event == null ? "#2563EB" : event.color;

        root = ui.vertical();
        root.setBackgroundColor(Color.parseColor("#F8FAFC"));
        root.setPadding(ui.dp(20), ui.dp(20), ui.dp(20), ui.dp(20));
        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(root);
        setContentView(scrollView);

        addTopBar(event == null ? text.addCountdownTitle() : text.editCountdownTitle(), v -> showHome());

        addLabel(text.eventName());
        titleInput = ui.input(text.eventNameHint());
        titleInput.setText(event == null ? "" : event.title);
        root.addView(titleInput, ui.matchWrap());

        ui.addSpace(root, 16);
        addLabel(text.targetDate());
        dateValue = ui.pill(calculator.formatDate(selectedDate, language), "#FFFFFF", "#0F172A");
        dateValue.setGravity(Gravity.CENTER_VERTICAL);
        dateValue.setPadding(ui.dp(16), ui.dp(14), ui.dp(16), ui.dp(14));
        dateValue.setOnClickListener(v -> pickDate());
        root.addView(dateValue, ui.matchWrap());

        ui.addSpace(root, 16);
        addLabel(text.color());
        colorRow = ui.horizontal();
        colorRow.setGravity(Gravity.CENTER_VERTICAL);
        root.addView(colorRow, ui.matchWrap());
        renderColors();

        ui.addSpace(root, 16);
        addLabel(text.note());
        noteInput = ui.input(text.optional());
        noteInput.setText(event == null ? "" : event.note);
        noteInput.setMinLines(3);
        noteInput.setGravity(Gravity.TOP);
        root.addView(noteInput, ui.matchWrap());

        ui.addSpace(root, 16);
        repeatYearlyInput = new CheckBox(this);
        repeatYearlyInput.setText(text.repeatYearly());
        repeatYearlyInput.setTextSize(16);
        repeatYearlyInput.setTextColor(Color.parseColor("#0F172A"));
        repeatYearlyInput.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        repeatYearlyInput.setChecked(event != null && event.repeatYearly);
        repeatYearlyInput.setPadding(0, ui.dp(8), 0, ui.dp(2));
        root.addView(repeatYearlyInput, ui.matchWrap());
        TextView repeatHelp = ui.text(text.repeatYearlyDetail(), 14, "#64748B", Typeface.NORMAL);
        root.addView(repeatHelp, ui.matchWrap());

        ui.addSpace(root, 24);
        Button save = ui.primaryButton(text.save());
        save.setOnClickListener(v -> saveEditor());
        root.addView(save, ui.matchWrap());

        if (event != null) {
            ui.addSpace(root, 12);
            Button delete = ui.secondaryButton(text.delete());
            delete.setTextColor(Color.parseColor("#DC2626"));
            delete.setOnClickListener(v -> confirmDelete(event));
            root.addView(delete, ui.matchWrap());
        }
    }

    private void addTopBar(String title, View.OnClickListener backAction) {
        LinearLayout bar = ui.horizontal();
        bar.setGravity(Gravity.CENTER_VERTICAL);
        root.addView(bar, ui.matchWrap());

        TextView back = ui.pill(text.back(), "#E2E8F0", "#0F172A");
        back.setOnClickListener(backAction);
        bar.addView(back);

        TextView heading = ui.text(title, 24, "#0F172A", Typeface.BOLD);
        heading.setGravity(Gravity.END);
        bar.addView(heading, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        ui.addSpace(root, 24);
    }

    private void pickDate() {
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate = LocalDate.of(year, month + 1, dayOfMonth);
                    dateValue.setText(calculator.formatDate(selectedDate, language));
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
        boolean repeatYearly = repeatYearlyInput.isChecked();
        if (title.isEmpty()) {
            titleInput.setError(text.titleRequired());
            titleInput.requestFocus();
            InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.showSoftInput(titleInput, InputMethodManager.SHOW_IMPLICIT);
            }
            return;
        }

        long now = System.currentTimeMillis();
        if (editingEvent == null) {
            events.add(new CountdownEvent(UUID.randomUUID().toString(), title, selectedDate, note, selectedColor, repeatYearly, now, now));
        } else {
            editingEvent.title = title;
            editingEvent.targetDate = selectedDate;
            editingEvent.note = note;
            editingEvent.color = selectedColor;
            editingEvent.repeatYearly = repeatYearly;
            editingEvent.updatedAt = now;
        }
        store.saveEvents(events);
        showHome();
    }

    private void confirmDelete(CountdownEvent event) {
        new AlertDialog.Builder(this)
                .setTitle(text.deleteCountdownQuestion())
                .setMessage(text.deleteCountdownMessage())
                .setPositiveButton(text.delete(), (dialog, which) -> {
                    events.remove(event);
                    store.saveEvents(events);
                    showHome();
                })
                .setNegativeButton(text.cancel(), null)
                .show();
    }

    private void addLabel(String value) {
        TextView label = ui.text(value, 14, "#64748B", Typeface.BOLD);
        root.addView(label);
        ui.addSpace(root, 8);
    }

    // Tapping anywhere outside the focused input (blank space, buttons, the scroll area)
    // dismisses the soft keyboard and drops focus, so the editor does not stay stuck
    // behind the keyboard.
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View focused = getCurrentFocus();
            if (focused instanceof EditText && !touchInside(focused, event)) {
                focused.clearFocus();
                InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (manager != null) {
                    manager.hideSoftInputFromWindow(focused.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    private boolean touchInside(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        Rect bounds = new Rect(location[0], location[1],
                location[0] + view.getWidth(), location[1] + view.getHeight());
        return bounds.contains((int) event.getRawX(), (int) event.getRawY());
    }

    @Override
    @SuppressLint("GestureBackNavigation")
    @SuppressWarnings("deprecation")
    public void onBackPressed() {
        handleBackNavigation();
    }

    private void handleBackNavigation() {
        if (currentScreen == Screen.HOME) {
            finish();
            return;
        }
        showHome();
    }

    private enum Screen {
        HOME,
        EDITOR
    }
}
