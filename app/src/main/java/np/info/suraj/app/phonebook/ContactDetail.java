package np.info.suraj.app.phonebook;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import model.Contact;
import model.Person;
import sqlite.ContactHelper;
import sqlite.PersonHelper;

public class ContactDetail extends AppCompatActivity {

    RelativeLayout detail_container;

    LinearLayout detail_mobile_layout;
    LinearLayout detail_home_layout;
    LinearLayout detail_street_layout;
    LinearLayout detail_city_layout;
    LinearLayout detail_country_layout;

    ImageView detail_user_image;

    TextView detail_full_name;
    TextView detail_mobile_number;
    TextView detail_home_number;
    TextView detail_street;
    TextView detail_city;
    TextView detail_country;
    TextView detail_about;

    ImageButton editImageButton, deleteImageButton;

    int personId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        detail_container = (RelativeLayout) findViewById(R.id.detail_container);
        detail_mobile_layout = (LinearLayout) findViewById(R.id.detail_mobile_layout);
        detail_home_layout = (LinearLayout) findViewById(R.id.detail_home_layout);
        detail_street_layout = (LinearLayout) findViewById(R.id.detail_street_layout);
        detail_city_layout = (LinearLayout) findViewById(R.id.detail_city_layout);
        detail_country_layout = (LinearLayout) findViewById(R.id.detail_country_layout);

        detail_user_image = (ImageView) findViewById(R.id.detail_user_image);

        detail_full_name = (TextView) findViewById(R.id.detail_full_name);
        detail_mobile_number = (TextView) findViewById(R.id.detail_mobile_number);
        detail_home_number = (TextView) findViewById(R.id.detail_home_number);
        detail_street = (TextView) findViewById(R.id.detail_street);
        detail_city = (TextView) findViewById(R.id.detail_city);
        detail_country = (TextView) findViewById(R.id.detail_country);
        detail_about = (TextView) findViewById(R.id.detail_about);

        editImageButton = (ImageButton) findViewById(R.id.editImageButton);
        deleteImageButton = (ImageButton) findViewById(R.id.deleteImageButton);

        Intent intent = this.getIntent();
        Bundle extra = intent.getExtras();

        personId = extra.getInt("personId");

        populate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populate();
    }

    private void populate() {
        final PersonHelper personHelper = new PersonHelper(this);
        final ContactHelper contactHelper = new ContactHelper(this);
        final Person person = personHelper.getPerson(personId);

        if(person != null) {
            String imagePath = person.getImagePath();
            String firstName = person.getFirstName();
            String lastName = person.getLastName();
            String street = person.getStreet();
            String city = person.getCity();
            String country = person.getCountry();
            String about = person.getAbout();

            if(imagePath.equals("")) {
                detail_user_image.setVisibility(View.GONE);
            } else {
                detail_user_image.setImageBitmap(BitmapFactory.decodeFile(person.getImagePath()));
                detail_user_image.setVisibility(View.VISIBLE);
            }

            detail_full_name.setText(String.format("%s %s", firstName, lastName));
            detail_mobile_number.setText("");
            detail_home_number.setText("");

            if(street.equals("")) {
                detail_street_layout.setVisibility(View.GONE);
            } else {
                detail_street.setText(street);
                detail_street.setVisibility(View.VISIBLE);
            }

            if(city.equals("")) {
                detail_city_layout.setVisibility(View.GONE);
            } else {
                detail_city.setText(city);
                detail_city.setVisibility(View.VISIBLE);
            }

            if(country.equals("")) {
                detail_country_layout.setVisibility(View.GONE);
            } else {
                detail_country.setText(country);
                detail_country.setVisibility(View.VISIBLE);
            }

            if(about.equals("")) {
                detail_about.setVisibility(View.GONE);
            } else {
                detail_about.setText(about);
                detail_about.setVisibility(View.VISIBLE);
            }

            ArrayList<Contact> personContacts = contactHelper.getPersonContacts(person.getId());

            if(personContacts != null) {
                boolean m=false, h=false;
                for(Contact contact : personContacts) {
                    String contactNumber = contact.getContactNumber();
                    String contactType = contact.getContactType();
                    Log.d("contact", contact.getContactType());
                    if(contactType.equals("mobile")) {
                        detail_mobile_number.setText(contactNumber);
                        m = true;
                    } else if(contactType.equals("home")) {
                        detail_home_number.setText(contactNumber);
                        h = true;
                    }
                }

                if(!m)
                    detail_mobile_layout.setVisibility(View.GONE);

                if(!h)
                    detail_home_layout.setVisibility(View.GONE);

                if(m && h) {
                    detail_mobile_layout.setVisibility(View.VISIBLE);
                    detail_home_layout.setVisibility(View.VISIBLE);
                }
            } else {
                detail_mobile_layout.setVisibility(View.GONE);
                detail_home_layout.setVisibility(View.GONE);
            }

            //edit
            editImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(getApplicationContext(), EditContactActivity.class);
                    intent1.putExtra("personId", person.getId());
                    startActivity(intent1);
                }
            });

            //delete
            deleteImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean deletePerson = personHelper.deletePerson(person);

                    if(deletePerson) {
                        contactHelper.deletePersonContacts(person);

                        Toast.makeText(getApplicationContext(), "Contact deleted successfully", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Error deleting contact", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
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
