package com.proxime;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewEvent extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_event);
        TextView nameView = (TextView) findViewById(R.id.event_name);
        nameView.setText(getIntent().getStringExtra("name"));


    }


}