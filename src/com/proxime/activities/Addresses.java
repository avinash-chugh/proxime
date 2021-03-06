package com.proxime.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class Addresses extends ListActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, getListItems()));

        ListView listView = getListView();
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                finish();
            }
        });
    }

    protected String[] getListItems() {
        String[] addresses = new String[10];
        for (int i = 0; i < addresses.length; i++) {
            addresses[i] = "Address " + (i + 1);
        }
        return addresses;
    }

}