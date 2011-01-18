package com.proxime.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.proxime.R;
import com.proxime.adapters.EventListAdapter;
import com.proxime.entities.Event;
import com.proxime.repositories.EventRepository;

import java.util.List;

public class Events extends Activity {
    private static final int NEW_EVENT = R.id.add_event;
    private static final int EDIT_EVENT = R.id.edit_event;
    private static final int ABOUT_DIALOG = R.id.about_proxime;
    private EventRepository eventRepository;
    private ListView eventsView;
    private EventListAdapter adapter;
    private EditText searchView;

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

        switch (requestCode) {
            case NEW_EVENT:  {
                Event event = getResult(data);
                showStatus(R.string.toast_event_created, event);
                adapter.add(event);
                break;
            }
            case EDIT_EVENT: {
                Event event = getResult(data);
                showStatus(R.string.toast_event_modified, event);
                loadEvents();
                break;
            }
        }
    }

    private Event getResult(Intent data) {
        return (Event) data.getExtras().get("event");
    }

    private void showStatus(int message, Event event) {
        Toast.makeText(this, getString(message, event.getName()), Toast.LENGTH_SHORT).show();
    }

    private void loadEvents() {
        List<Event> events = eventRepository.loadAll();

        eventsView = (ListView) findViewById(R.id.eventsList);
        eventsView.setTextFilterEnabled(true);

        adapter = new EventListAdapter(this, events);
        eventsView.setAdapter(adapter);
    }

    private void hookUpListeners() {
        ImageButton createButton = (ImageButton) findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                newEvent();
            }
        });

        eventsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                long id = ((EventListAdapter.EventViewHolder) view.getTag()).id;
                startActivity(new Intent(getApplicationContext(), ViewEvent.class).putExtra("event_id", id));
            }
        });

        registerForContextMenu(eventsView);

        searchView = (EditText) findViewById(R.id.search_event);
        searchView.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int count, int after) {
                adapter.filter(s.toString());
            }

            public void afterTextChanged(Editable editable) {
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.events_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Event event = adapter.getItem(info.position);

        switch (item.getItemId()) {
            case R.id.edit_event: {
                editEvent(event.getId());
                break;
            }
            case R.id.delete_event: {
                deleteEvent(event);
                break;
            }
        }
        return super.onContextItemSelected(item);

    }

    private void newEvent() {
        Intent intent = new Intent(this, EditEvent.class);
        startActivityForResult(intent, NEW_EVENT);
    }

    private void editEvent(long id) {
        Intent intent = new Intent(getApplicationContext(), EditEvent.class).putExtra("event_id", id);
        startActivityForResult(intent, EDIT_EVENT);
    }

    private void deleteEvent(Event event) {
        eventRepository.delete(event.getId());
        adapter.remove(event);
        showStatus(R.string.toast_event_removed, event);
    }
}
