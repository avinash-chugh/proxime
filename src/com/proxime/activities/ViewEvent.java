package com.proxime.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.proxime.R;
import com.proxime.entities.Event;
import com.proxime.repositories.EventRepository;

public class ViewEvent extends Activity {
    private EventRepository eventRepository;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_view);

        setDependencies();
        updateView();
    }

    private void updateView() {

        long id = getIntent().getLongExtra("event_id", -1);
        Event event = eventRepository.load(id);
        setTitle(event.getName());
//        setText(R.id.view_event_name, event.getName());
        setText(R.id.view_event_message,event.getMessage());
        if(event.getContact() != null) setText(R.id.view_event_contact, event.getContact().getName());
        if(event.getLocation() != null) setText(R.id.view_event_location, event.getLocation().getName());
    }

    private void setText(int id, String text) {
        ((TextView)findViewById(id)).setText(text);
    }

    private void setDependencies() {
        eventRepository = new EventRepository(this);
    }


}