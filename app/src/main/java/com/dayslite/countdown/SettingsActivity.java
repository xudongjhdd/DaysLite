package com.dayslite.countdown;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingsActivity extends Activity {
    private CountdownStore store;
    private UiKit ui;
    private AppLanguage language;
    private AppText text;
    private LinearLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UiKit.applyLightNavigationBar(this);
        store = new CountdownStore(this);
        ui = new UiKit(this);
        language = store.loadLanguage();
        text = new AppText(language);
        showSettings();
    }

    private void showSettings() {
        root = ui.vertical();
        root.setBackgroundColor(Color.parseColor("#F8FAFC"));
        root.setPadding(ui.dp(20), ui.dp(20), ui.dp(20), ui.dp(20));
        setContentView(root);

        addTopBar(text.settingsTitle(), v -> finish());
        root.addView(settingsItem(text.privacyPolicy(), text.privacyDetail(),
                v -> startActivity(new Intent(this, PrivacyPolicyActivity.class))), ui.matchWrap());
        ui.addSpace(root, 12);
        root.addView(settingsItem(text.localStorage(), text.localStorageDetail(), null), ui.matchWrap());
        ui.addSpace(root, 12);
        root.addView(settingsItem(text.language(), text.languageDetail(), v -> toggleLanguage()), ui.matchWrap());
        ui.addSpace(root, 12);
        root.addView(settingsItem(text.version(), BuildConfig.VERSION_NAME, null), ui.matchWrap());
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

        TextView back = ui.pill(text.back(), "#E2E8F0", "#0F172A");
        back.setOnClickListener(backAction);
        bar.addView(back);

        TextView heading = ui.text(title, 24, "#0F172A", Typeface.BOLD);
        heading.setGravity(Gravity.END);
        bar.addView(heading, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        ui.addSpace(root, 24);
    }

    private void toggleLanguage() {
        language = language.toggled();
        text = new AppText(language);
        store.saveLanguage(language);
        showSettings();
    }
}
