package com.proxime;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.proxime.repositories.EventsRepository;

public class ViewEvent extends Activity {
    private EventsRepository eventsRepository;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_event);

        setDependencies();
        updateView();
    }

    private void updateView() {
        long id = getIntent().getLongExtra("event_id", -1);
        Event event = eventsRepository.load(id);
        setText(R.id.view_event_name,event.getName());
        setText(R.id.view_event_message,event.getMessage());
        if(event.getContact() != null) setText(R.id.view_event_contact, event.getContact().getName());
    }

    private void setText(int id, String text) {
        ((TextView)findViewById(id)).setText(text);
    }

    private void setDependencies() {
        eventsRepository = new EventsRepository(this);
    }


}