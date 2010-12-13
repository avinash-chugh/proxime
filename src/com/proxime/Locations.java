package com.proxime;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class Locations extends Activity {
    private ListView locationsView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locations);
        locationsView = (ListView) findViewById(R.id.locationsList);
        locationsView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, new String[]{"Office", "Home", "Grocery Shop" }));
        locationsView.setTextFilterEnabled(true);
        HookUpEvents();
    }

    private void HookUpEvents() {
        Button createButton = (Button) findViewById(R.id.createLocationButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showDialog(0);
            }
        });

        locationsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                finish();
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        final CharSequence[] items = {"Use current location", "Use Map"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("How do you want to create location ?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item)
                {
                    case 0:
                        ShowAddresses();
                        break;
                    case 1:
                        ShowMap();
                        break;
                }
            }
        });
        return builder.create();
    }

    private void ShowAddresses() {
       startActivity(new Intent(getApplicationContext(),AddressList.class));
    }

    private void ShowMap() {
        startActivity(new Intent(getApplicationContext(),LocationFromMap.class));
    }
}