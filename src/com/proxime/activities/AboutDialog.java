package com.proxime.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.text.Html;
import android.text.util.Linkify;
import android.widget.ScrollView;
import android.widget.TextView;
import com.proxime.R;

public class AboutDialog {
    private Activity activity;

    public AboutDialog(Activity activity) {

        this.activity = activity;
    }

    public Dialog Show() {
        ScrollView scrollView = new ScrollView(activity);
        TextView textView = new TextView(activity);

        textView.setAutoLinkMask(Linkify.ALL);
        textView.setPadding(5, 5, 5, 5);
        textView.setTextColor(Color.WHITE);
        textView.setText(Html.fromHtml(activity.getString(R.string.about_message)));

        scrollView.addView(textView);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.about_title));
        builder.setView(scrollView);
        builder.setPositiveButton(android.R.string.ok, null);

        return builder.create();
    }
}
