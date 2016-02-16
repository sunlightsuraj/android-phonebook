package np.info.suraj.app.phonebook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import model.Contact;
import model.Person;
import sqlite.ContactHelper;
import sqlite.PersonHelper;

public class EditContactActivity extends AppCompatActivity {

    private ImageView edit_user_image;

    private TextView edit_first_name,
            edit_last_name,
            edit_mobile_number,
            edit_home_number,
            edit_street,
            edit_city,
            edit_country,
            edit_about;

    private Button edit_btn, edit_reset_btn;

    private String strImagePath = "";
    private String strFirstName;
    private String strLastName;
    private String strMobileNumber;
    private String strHomeNumber;
    private String strStreetAddress;
    private String strCity;
    private String strCountry;
    private String strAbout;

    private int personId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        edit_user_image = (ImageView) findViewById(R.id.edit_user_image);

        edit_first_name = (TextView) findViewById(R.id.edit_first_name);
        edit_last_name = (TextView) findViewById(R.id.edit_last_name);
        edit_mobile_number = (TextView) findViewById(R.id.edit_mobile_number);
        edit_home_number = (TextView) findViewById(R.id.edit_home_number);
        edit_street = (TextView) findViewById(R.id.edit_street);
        edit_city = (TextView) findViewById(R.id.edit_city);
        edit_country = (TextView) findViewById(R.id.edit_country);
        edit_about = (TextView) findViewById(R.id.edit_about);

        edit_btn = (Button) findViewById(R.id.edit_btn);
        edit_reset_btn = (Button) findViewById(R.id.edit_reset_btn);

        personId = getIntent().getIntExtra("personId", 0);

        PersonHelper personHelper = new PersonHelper(this);
        ContactHelper contactHelper = new ContactHelper(this);
        final Person person = personHelper.getPerson(personId);

        if(person != null) {
            personId = person.getId();
            strImagePath = person.getImagePath();
            if(!person.getImagePath().equals(""))
                edit_user_image.setImageBitmap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(person.getImagePath()), 100, 100));

            edit_first_name.setText(person.getFirstName());
            edit_last_name.setText(person.getLastName());
            edit_street.setText(person.getStreet());
            edit_city.setText(person.getCity());
            edit_country.setText(person.getCountry());
            edit_about.setText(person.getAbout());

            ArrayList<Contact> contacts = contactHelper.getPersonContacts(person.getId());

            for (Contact contact : contacts) {
                if(contact.getContactType().equals("mobile")) {
                    edit_mobile_number.setText(contact.getContactNumber());
                } else if(contact.getContactType().equals("home")) {
                    edit_home_number.setText(contact.getContactNumber());
                }
            }
        }

        edit_reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_user_image.setImageResource(R.drawable.user);
                edit_first_name.setText("");
                edit_last_name.setText("");
                edit_mobile_number.setText("");
                edit_home_number.setText("");
                edit_street.setText("");
                edit_city.setText("");
                edit_country.setText("");
                edit_about.setText("");
            }
        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strFirstName = edit_first_name.getText().toString();
                strLastName = edit_last_name.getText().toString();
                strMobileNumber = edit_mobile_number.getText().toString();
                strHomeNumber = edit_home_number.getText().toString();
                strStreetAddress = edit_street.getText().toString();
                strCity = edit_city.getText().toString();
                strCountry = edit_country.getText().toString();
                strAbout = edit_about.getText().toString();

                try {
                    if(!strFirstName.equals("") || !strLastName.equals("")) {
                        if(!strMobileNumber.equals("") || !strHomeNumber.equals("")) {
                            Log.d("imagePath", strImagePath);
                            Person person1 = new Person(personId, strFirstName,strLastName, strImagePath ,strStreetAddress,strCity,strCountry,strAbout);
                            PersonHelper personHelper = new PersonHelper(getApplicationContext());
                            int person_id = personHelper.updatePerson(person1);
                            Log.d("personid", String.valueOf(personId));
                            if(person_id > 0) {
                                //person.setId(personId);
                                ContactHelper contactHelper = new ContactHelper(getApplicationContext());

                                if(contactHelper.deletePersonContacts(person1)) {

                                    if (!strMobileNumber.equals("")) {
                                        Contact mobileContact = new Contact(personId, strMobileNumber, "mobile");
                                        contactHelper.addContact(mobileContact);
                                    }

                                    if (!strHomeNumber.equals("")) {
                                        Contact homeContact = new Contact(personId, strHomeNumber, "home");
                                        contactHelper.addContact(homeContact);
                                    }

                                    Toast.makeText(getApplicationContext(), "Contact Updated Successfully", Toast.LENGTH_LONG).show();
                                } else {
                                    throw new Exception("Error updating contact numbers");
                                }
                            } else {
                                throw new Exception("Error adding person detail");
                            }
                        } else {
                            throw new Exception("Please Enter Mobile or Home Number!");
                        }
                    } else {
                        throw new Exception("Please Enter First Name or Last Name!");
                    }
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

        edit_user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                if(permissionCheck == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(EditContactActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if(requestCode == 2) {
                /*imageUri = data.getData();*/
                /*Log.d("userimage", String.valueOf(imageUri));*/

                Uri imageUri = data.getData();
                edit_user_image.setImageURI(imageUri);

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(imageUri, filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    strImagePath = cursor.getString(columnIndex);
                    cursor.close();
                    Log.i("imagePath", strImagePath);
                } else {
                    strImagePath = "";
                    Log.e("error", "Error");
                }
            }
        }
    }
}
