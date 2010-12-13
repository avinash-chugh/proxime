package com.proxime;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class Events extends Activity {
    private ListView eventsView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        eventsView = (ListView) findViewById(R.id.eventsList);
        eventsView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, new String[]{"Reached Office", "Submit Broadband Bill", "Pay Maintenance" }));
        eventsView.setTextFilterEnabled(true);
        HookUpEvents();

    }

    private void HookUpEvents() {
        Button createButton = (Button) findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showDialog(0);
            }
        });

        eventsView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object itemAtPosition = adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getApplicationContext(), ViewEvent.class);
                intent.putExtra("name", itemAtPosition.toString());
                startActivity(intent);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        final CharSequence[] items = {"Send SMS", "Notify Self"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Which event ?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                startActivity(new Intent(getApplicationContext(), EditEvent.class));
            }
        });
        return builder.create();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.events_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Class activityClass = item.getItemId() == R.id.goto_locations ? Locations.class : About.class;
        startActivity(new Intent(getApplicationContext(),activityClass));
        return true;
    }
}
