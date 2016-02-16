package sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import model.Contact;
import model.Person;

/**
 * Created by suraj on 1/16/16.
 * Email suraj.sht@hotmail.com
 */
public class ContactHelper extends MySQLiteOpenHelper {
    private static final String id = "id";
    private static final String personId = "person_id";
    private static final String contactNumber = "contact_number";
    private static final String contactType = "contact_type";

    private static final String[] COLUMNS = {id,personId,contactNumber,contactType};

    public ContactHelper(Context context) {
        super(context);
    }

    public int addContact(Contact mobileContact) {
        //SQLiteDatabase
        SQLiteDatabase db = this.getWritableDatabase();

        //ContentValues
        ContentValues values = new ContentValues();
        values.put(personId,mobileContact.getPersonId());
        values.put(contactNumber,mobileContact.getContactNumber());
        values.put(contactType, mobileContact.getContactType());

        //insert
        int contactId = (int) db.insert(T_CONTACT, null, values);

        db.close();
        return contactId;
    }

    public ArrayList<Contact> getPersonContacts(int personId) {
        ArrayList<Contact> personContacts = new ArrayList<Contact>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(T_CONTACT,
                COLUMNS,
                " person_id = ?",
                new String[] {String.valueOf(personId)},
                null,
                null,
                null,
                null);

        if(cursor != null) {
            Contact contact = null;
            if(cursor.moveToFirst()) {
                do {
                    contact = new Contact();
                    contact.setId(Integer.parseInt(cursor.getString(0)));
                    contact.setPersonId(Integer.parseInt(cursor.getString(1)));
                    contact.setContactNumber(cursor.getString(2));
                    contact.setContactType(cursor.getString(3));

                    personContacts.add(contact);
                } while(cursor.moveToNext());

                return personContacts;
            }
        }

        return null;
    }

    public boolean deletePersonContacts(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(T_CONTACT,
                personId + " = ?",
                new String[] {String.valueOf(person.getId())});

        db.close();

        return true;
    }
}
