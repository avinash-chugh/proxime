package com.proxime;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;

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
                if (cursor.getString(1).equals(Phone.TYPE_MOBILE)) return cursor.getString(0);
            }
            return null;
        } finally {
            if(cursor != null) cursor.close();
        }
    }
}
