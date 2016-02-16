package sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by suraj on 1/16/16.
 * Email suraj.sht@hotmail.com
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "phonebook";
    private static final int DB_VERSION = 1;

    protected static final String T_PERSON = "person";
    protected static final String T_CONTACT = "contact";

    public MySQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private void createPersonTable(SQLiteDatabase db) {
        String create_person_table = "create table person (" +
                "id integer primary key autoincrement," +
                "first_name varchar(50) not null," +
                "last_name varchar(50) not null," +
                "image varchar(255) not null," +
                "street varchar(50) not null," +
                "city varchar(50) not null," +
                "country varchar(50) not null," +
                "about varchar(255) not null )";

        db.execSQL(create_person_table);
    }

    private void createContactTable(SQLiteDatabase db) {
        String create_contact_table = "create table contact (" +
                "id integer primary key autoincrement," +
                "person_id integer not null," +
                "contact_number integer not null," +
                "contact_type varchar(50) not null )";

        db.execSQL(create_contact_table);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createContactTable(db);
        createPersonTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists person");
        db.execSQL("drop table if exists contact");

        this.onCreate(db);
    }
}
