package sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import model.Person;

/**
 * Created by suraj on 1/16/16.
 * Email suraj.sht@hotmail.com
 */
public class PersonHelper extends MySQLiteOpenHelper {
    private static final String id = "id";
    private static final String firstName = "first_name";
    private static final String lastName = "last_name";
    private static final String image = "image";
    private static final String street = "street";
    private static final String city = "city";
    private static final String country = "country";
    private static final String about = "about";

    private static final String[] COLUMNS = {id,firstName,lastName,image,street,city,country,about};

    public PersonHelper(Context context) {
        super(context);
    }

    public ArrayList<Person> getAllPerson() {
        ArrayList<Person> persons = new ArrayList<Person>();

        //build the query
        /*String query = "select *from " + T_PERSON;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);*/

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(T_PERSON,
                COLUMNS,
                null,
                null,
                null,
                null,
                firstName,
                null);

        Person person = null;
        if(cursor.moveToFirst()) {
            do {
                person = new Person();
                person.setId(Integer.parseInt(cursor.getString(0)));
                person.setFirstName(cursor.getString(1));
                person.setLastName(cursor.getString(2));
                //person.setImage(cursor.getString(3).equals("") ? null : Uri.parse(cursor.getString(3)));
                person.setImagePath(cursor.getString(3));
                person.setStreet(cursor.getString(4));
                person.setCity(cursor.getString(5));
                person.setCountry(cursor.getString(6));
                person.setAbout(cursor.getString(7));

                persons.add(person);
                //Log.d("userimage", String.valueOf(person.getImage()));
            } while (cursor.moveToNext());
        }
        db.close();
        return persons;
    }

    public Person getPerson(int id) {
        Person person = null;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(T_PERSON,
                COLUMNS,
                " id = ?",
                new String[] {String.valueOf(id)},
                null,
                null,
                null,
                null);

        if(cursor != null) {
            cursor.moveToFirst();

            person = new Person();
            person.setId(Integer.parseInt(cursor.getString(0)));
            person.setFirstName(cursor.getString(1));
            person.setLastName(cursor.getString(2));
            person.setImagePath(cursor.getString(3));
            person.setStreet(cursor.getString(4));
            person.setCity(cursor.getString(5));
            person.setCountry(cursor.getString(6));
            person.setAbout(cursor.getString(7));
        }

        db.close();
        return person;
    }

    public int addPerson(Person person) {
        //reference to db
        SQLiteDatabase db = this.getWritableDatabase();

        //Content value
        ContentValues values = new ContentValues();
        values.put(firstName, person.getFirstName());
        values.put(lastName, person.getLastName());
        //values.put(image, person.getImage() != null ? person.getImage().toString() : "");
        values.put(image, person.getImagePath());
        values.put(street, person.getStreet());
        values.put(city, person.getCity());
        values.put(country, person.getCountry());
        values.put(about, person.getAbout());

        //insert
        int person_id = (int) db.insert(T_PERSON, null, values);

        //close
        db.close();

        return person_id;
    }

    public int updatePerson(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(firstName, person.getFirstName());
        values.put(lastName, person.getLastName());
        values.put(image, person.getImagePath());
        values.put(street, person.getStreet());
        values.put(city, person.getCity());
        values.put(country, person.getCountry());
        values.put(about, person.getAbout());

        int i = db.update(T_PERSON,
                values,
                id + " = ?",
                new String[] {String.valueOf(person.getId())});

        db.close();

        return i;
    }

    public boolean deletePerson(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();

        int d = db.delete(T_PERSON,
                id + " = ?",
                new String[] {String.valueOf(person.getId())});
        Log.i("delete", String.valueOf(d));

        db.close();

        return true;
    }
}
