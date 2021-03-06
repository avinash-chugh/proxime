package com.proxime.repositories;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import com.proxime.entities.Contact;

public class ContactRepository {
    private ContentResolver contentResolver;

    public ContactRepository(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public Contact getContact(Uri uri) {

        Cursor c = contentResolver.query(uri, null, null, null, null);
        if (!c.moveToFirst()) return null;
        String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
        Contact contact = new Contact(name, uri);
        String rawId = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
        c.close();
        contact.setPhoneNumber(getPhoneNumber(rawId));
        return contact;
    }

    private String getPhoneNumber(String rawId) {
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(Phone.CONTENT_URI, new String[]{Phone.NUMBER, Phone.TYPE}, Phone.RAW_CONTACT_ID + " = ?", new String[]{rawId}, null);
            while (cursor.moveToNext()) {
            }
            return null;
        } finally {
            if(cursor != null) cursor.close();
        }
    }
}
