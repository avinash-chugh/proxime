package com.proxime.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import com.proxime.R;
import com.proxime.entities.Contact;
import com.proxime.entities.Event;
import com.proxime.entities.Location;
import com.proxime.repositories.ContactRepository;
import com.proxime.repositories.EventRepository;
import com.proxime.services.LocationTracker;

public class EditEvent extends Activity {

    private static final int PICK_CONTACT = 0;
    private static final int GET_LOCATION = 1;

    private ContactRepository contactRepository;
    private EventRepository eventRepository;
    private Event event = new Event();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_edit);
        setDependencies();
        hookUpEvents();
        loadEvent();
    }

    private void loadEvent() {
        long id = getIntent().getLongExtra("event_id", -1);
        if (id < 0) return;

        event = eventRepository.load(id);

        setTitle(event.getName());
        setText(R.id.edit_event_name, event.getName());
        setText(R.id.edit_event_message, event.getMessage());
        if (event.getContact() != null) setText(R.id.edit_event_contact, event.getContact().getName());
        if (event.getLocation() != null) setText(R.id.edit_event_location, event.getLocation().getName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case PICK_CONTACT:
                Contact contact = contactRepository.getContact(data.getData());
                event.setContact(contact);
                break;
            case GET_LOCATION:
                Bundle extras = data.getExtras();
                Location location = (Location) extras.get("location");
                event.setLocation(location);
                break;
        }
        updateDisplay();
    }

    private void setDependencies() {
        contactRepository = new ContactRepository(this.getContentResolver());
        eventRepository = new EventRepository(this);
    }

    private void hookUpEvents() {

        findViewById(R.id.edit_event_add_contact).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

        findViewById(R.id.edit_event_add_location).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(), Locations.class), GET_LOCATION);
            }
        });

        findViewById(R.id.edit_event_cancel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.edit_event_save).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                event.setName(getViewText(R.id.edit_event_name));
                event.setMessage(getViewText(R.id.edit_event_message));
                eventRepository.save(event);
                
                startService(new Intent(EditEvent.this, LocationTracker.class).putExtra("event", event).putExtra("action", "add"));
                setResult(RESULT_OK, new Intent().putExtra("event", event));
                finish();
            }
        });

        View.OnClickListener radioListener = new View.OnClickListener() {
            public void onClick(View view) {
                int id = view.getId();
                if(id == R.id.edit_event_notify_self){
                   event.setType(Event.NOTIFY_SELF);
                   setVisibilityForContact(View.GONE);
                }else {
                   event.setType(Event.SEND_MESSAGE);
                   setVisibilityForContact(View.VISIBLE);
                }
            }

            private void setVisibilityForContact(int visibility) {
                findViewById(R.id.edit_event_contact_view_group).setVisibility(visibility);
                findViewById(R.id.edit_event_contact).setVisibility(visibility);
            }

        };
        findViewById(R.id.edit_event_notify_self).setOnClickListener(radioListener);
        findViewById(R.id.edit_event_send_message).setOnClickListener(radioListener);
        RadioButton button = (RadioButton) findViewById(R.id.edit_event_send_message);
        button.setChecked(true);
    }


    private void updateDisplay() {
        if (event.getLocation() != null)
            ((TextView) findViewById(R.id.edit_event_location)).setText(event.getLocation().getName());
        if (event.getContact() != null)
            ((TextView) findViewById(R.id.edit_event_contact)).setText(event.getContact().getName());
    }

    public String getViewText(int resourceId) {
        return ((TextView) findViewById(resourceId)).getText().toString();
    }

    private void setText(int id, String text) {
        ((TextView)findViewById(id)).setText(text);
    }
}