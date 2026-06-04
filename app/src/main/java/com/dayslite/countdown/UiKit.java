package com.dayslite.countdown;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

class UiKit {
    private final Context context;

    UiKit(Context context) {
        this.context = context;
    }

    EditText input(String hint) {
        EditText input = new EditText(context);
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

    Button primaryButton(String value) {
        Button button = new Button(context);
        button.setText(value);
        button.setAllCaps(false);
        button.setTextColor(Color.WHITE);
        button.setTextSize(16);
        button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        button.setPadding(dp(14), dp(10), dp(14), dp(10));
        button.setBackground(round("#2563EB", 14, "#2563EB"));
        return button;
    }

    Button secondaryButton(String value) {
        Button button = new Button(context);
        button.setText(value);
        button.setAllCaps(false);
        button.setTextSize(16);
        button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        button.setPadding(dp(14), dp(10), dp(14), dp(10));
        button.setBackground(round("#FFFFFF", 14, "#CBD5E1"));
        return button;
    }

    TextView pill(String value, String background, String foreground) {
        TextView view = text(value, 14, foreground, Typeface.BOLD);
        view.setPadding(dp(14), dp(9), dp(14), dp(9));
        view.setBackground(round(background, 99, "#CBD5E1"));
        return view;
    }

    TextView text(String value, int sp, String color, int style) {
        TextView text = new TextView(context);
        text.setText(value);
        text.setTextSize(sp);
        text.setTextColor(Color.parseColor(color));
        text.setTypeface(Typeface.DEFAULT, style);
        text.setLineSpacing(dp(2), 1);
        return text;
    }

    LinearLayout vertical() {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        return layout;
    }

    LinearLayout horizontal() {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        return layout;
    }

    LinearLayout.LayoutParams matchWrap() {
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    void addSpace(LinearLayout parent, int dp) {
        View spacer = new View(context);
        parent.addView(spacer, new LinearLayout.LayoutParams(1, dp(dp)));
    }

    GradientDrawable round(String fill, int radius, String stroke) {
        return round(fill, radius, stroke, 1);
    }

    GradientDrawable round(String fill, int radius, String stroke, int strokeWidth) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.parseColor(fill));
        drawable.setCornerRadius(dp(radius));
        if (strokeWidth > 0) {
            drawable.setStroke(dp(strokeWidth), Color.parseColor(stroke));
        }
        return drawable;
    }

    int dp(int value) {
        return Math.round(value * context.getResources().getDisplayMetrics().density);
    }
}
