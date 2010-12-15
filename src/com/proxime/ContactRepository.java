package com.proxime;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public class ContactRepository {
    private Activity activity;

    public ContactRepository(Activity activity) {
        this.activity = activity;
    }

    public Contact getContact(Uri uri) {
        Cursor c = activity.managedQuery(uri, null, null, null, null);
        if (c.moveToFirst()) {
            String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
            return new Contact(name,uri);
        }
        return null;
    }
}
