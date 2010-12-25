package com.proxime;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.proxime.repositories.EventRepository;

import java.util.List;

public class Events extends Activity {
    private EventRepository eventRepository;
    private static final int NEW_EVENT = 1;
    private ListView eventsView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setDependencies();
        loadEvents();
        hookUpListeners();
        createTrackerService();
    }

    private void createTrackerService() {
    }

    private void setDependencies() {
        eventRepository = new EventRepository(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK) return;
        if(requestCode == NEW_EVENT) {
            CustomListAdapter<Event> adapter = (CustomListAdapter<Event>) eventsView.getAdapter();
            Event event = (Event) data.getExtras().get("event");
            adapter.add(event);
        }
    }

    private void loadEvents() {
        List<Event> events = eventRepository.loadAll();
        eventsView = (ListView) findViewById(R.id.eventsList);
        eventsView.setTextFilterEnabled(true);
        eventsView.setAdapter(new CustomListAdapter<Event>(this,events));
    }

    private void hookUpListeners() {
        Button createButton = (Button) findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                showDialog(0);
                startActivityForResult(new Intent(getApplicationContext(), EditEvent.class), NEW_EVENT);

            }
        });

        eventsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                long id = ((CustomListAdapter.ViewHolder) view.getTag()).id;
                startActivity(new Intent(getApplicationContext(), ViewEvent.class).putExtra("event_id",id));
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
        Intent intent = new Intent(getApplicationContext(), activityClass);
        startActivity(intent);
        return true;
    }
}
