package com.proxime.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.proxime.R;
import com.proxime.adapters.CustomListAdapter;
import com.proxime.entities.Event;
import com.proxime.repositories.EventRepository;

import java.util.List;

public class Events extends Activity {
    private static final int NEW_EVENT = R.id.add_event;
    private static final int ABOUT_DIALOG = R.id.about_proxime;
    private EventRepository eventRepository;
    private ListView eventsView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);

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
        eventsView.setAdapter(new CustomListAdapter<Event>(this, events));
    }

    private void hookUpListeners() {
        Button createButton = (Button) findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                newEvent();
            }
        });

        eventsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                long id = ((CustomListAdapter.ViewHolder) view.getTag()).id;
                startActivity(new Intent(getApplicationContext(), ViewEvent.class).putExtra("event_id", id));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.events_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_event: {
                newEvent();
                break;
            }
            case R.id.view_locations: {
                Intent intent = new Intent(this, Locations.class);
                startActivity(intent);
                break;
            }
            case R.id.about_proxime: {
                showDialog(ABOUT_DIALOG);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Dialog onCreateDialog(int id, Bundle args) {
        switch (id) {
            case ABOUT_DIALOG: {
                return new AboutDialog(this).show();
            }
        }
        return super.onCreateDialog(id, args);
    }

    private void newEvent() {
//        showDialog(0);
        Intent intent = new Intent(this, EditEvent.class);
        startActivityForResult(intent, NEW_EVENT);
    }
}
