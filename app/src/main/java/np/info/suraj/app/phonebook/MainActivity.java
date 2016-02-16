package np.info.suraj.app.phonebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import model.Person;
import sqlite.PersonHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listContacts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listContacts();
    }

    public void listContacts() {
        ListView all_contact_list = (ListView) findViewById(R.id.all_contact_list);

        PersonHelper personHelper = new PersonHelper(getApplicationContext());
        final ArrayList<Person> allPerson = personHelper.getAllPerson();
        /*final ArrayList<String> personNameArray = new ArrayList<String>();
        final ArrayList<Integer> imgId = new ArrayList<Integer>();*/

        /*for(Person person : allPerson) {
            String firstName = person.getFirstName();
            String lastName = person.getLastName();
            String fullName = firstName + " " + lastName;
            personNameArray.add(fullName);
            imgId.add(R.drawable.user);
        }*/

        /*ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, personNameArray);
        all_contact_list.setAdapter(aa);*/

        CustomListAdapter adapter = new CustomListAdapter(this, allPerson);
        all_contact_list.setAdapter(adapter);

        all_contact_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Person p = allPerson.get(position);
                //Toast.makeText(getApplicationContext(), p.getId() + " " + p.getFirstName() + " " + p.getLastName() + " " + p.getImage() + " " + p.getStreet() + " " + p.getCity(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, ContactDetail.class);
                intent.putExtra("personId", p.getId());
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case R.id.addContactMenu:
                Intent intent = new Intent(this, AddContactActivity.class);
                startActivity(intent);
                break;
            default:
                return true;
        }
        return true;
    }
}
