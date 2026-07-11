package com.dayslite.countdown;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PrivacyPolicyActivity extends Activity {
    private static final String PRIVACY_URL_EN = "https://xudongjhdd.github.io/DaysLite/privacy-policy.html";
    private static final String PRIVACY_URL_ZH = "https://xudongjhdd.github.io/DaysLite/privacy-policy.zh.html";

    private UiKit ui;
    private AppText text;
    private WebView webView;
    private TextView errorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UiKit.applyLightNavigationBar(this);
        ui = new UiKit(this);
        AppLanguage language = new CountdownStore(this).loadLanguage();
        text = new AppText(language);
        String url = language == AppLanguage.CHINESE ? PRIVACY_URL_ZH : PRIVACY_URL_EN;

        LinearLayout root = ui.vertical();
        root.setBackgroundColor(Color.parseColor("#F8FAFC"));
        root.setPadding(ui.dp(20), ui.dp(20), ui.dp(20), ui.dp(20));
        setContentView(root);

        addTopBar(root, text.privacyPolicy(), v -> finish());

        errorView = ui.text(text.privacyLoadError(), 15, "#64748B", Typeface.NORMAL);
        errorView.setGravity(Gravity.CENTER);
        errorView.setPadding(ui.dp(8), ui.dp(40), ui.dp(8), ui.dp(8));
        errorView.setVisibility(View.GONE);
        root.addView(errorView, ui.matchWrap());

        webView = new WebView(this);
        webView.setBackgroundColor(Color.parseColor("#F8FAFC"));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                String scheme = uri.getScheme();
                // Keep http(s) inside the WebView; hand off mailto/tel and the like to the system.
                if ("http".equals(scheme) || "https".equals(scheme)) {
                    return false;
                }
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                } catch (Exception ignored) {
                    // No app available to handle the link; ignore.
                }
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, android.webkit.WebResourceError error) {
                // Only surface the error for the main page load, not sub-resources.
                if (request.isForMainFrame()) {
                    showError();
                }
            }
        });
        root.addView(webView, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        webView.loadUrl(url);
    }

    private void showError() {
        webView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
    }

    private void addTopBar(LinearLayout parent, String title, View.OnClickListener backAction) {
        LinearLayout bar = ui.horizontal();
        bar.setGravity(Gravity.CENTER_VERTICAL);
        parent.addView(bar, ui.matchWrap());

        TextView back = ui.pill(text.back(), "#E2E8F0", "#0F172A");
        back.setOnClickListener(backAction);
        bar.addView(back);

        TextView heading = ui.text(title, 24, "#0F172A", Typeface.BOLD);
        heading.setGravity(Gravity.END);
        bar.addView(heading, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        ui.addSpace(parent, 16);
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}
