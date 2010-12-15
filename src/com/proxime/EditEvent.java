package com.proxime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.proxime.repositories.EventsRepository;

public class EditEvent extends Activity {

    private static final int PICK_CONTACT = 0;
    private static final int GET_LOCATION = 1;

    private ContactRepository contactRepository;
    private EventsRepository eventsRepository;
    private Event event = new Event();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event);
        setDependencies();
        hookUpEvents();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_CONTACT:
                if (resultCode == Activity.RESULT_OK) {
                    Contact contact = contactRepository.getContact(data.getData());
                    event.setContact(contact);
                }
                break;
            case GET_LOCATION:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Location location = (Location) extras.get("location");
                    event.setLocation(location);
                }
                break;
        }
        display();
    }

    private void setDependencies() {
        contactRepository = new ContactRepository(this);
        eventsRepository = new EventsRepository(this);
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
                event.setName(getName());
                event.setMessage(getMessage());
                long id = eventsRepository.save(event);
                event.setId(id);
                setResult(RESULT_OK, new Intent().putExtra("event", event));
                finish();
            }
        });

    }

    private String getMessage() {
        return ((TextView) findViewById(R.id.edit_event_message)).getText().toString();
    }

    private void display() {
        if(event.getLocation() != null) ((TextView) findViewById(R.id.edit_event_location)).setText(event.getLocation().getName());
        if(event.getContact() != null)((TextView) findViewById(R.id.edit_event_contact)).setText(event.getContact().getName());
    }

    public String getName() {
        return ((EditText) findViewById(R.id.edit_event_name)).getText().toString();
    }
}