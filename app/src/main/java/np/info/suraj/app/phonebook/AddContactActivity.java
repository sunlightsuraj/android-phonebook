package np.info.suraj.app.phonebook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import model.Contact;
import model.Person;
import sqlite.ContactHelper;
import sqlite.PersonHelper;

public class AddContactActivity extends AppCompatActivity {
    private Button addButton;
    private ImageView userImage;
    private EditText firstName,lastName,mobileNumber,homeNumber,streetAddress,city,country,about;
    private String strFirstName = "",
            strLastName = "",
            strMobileNumber = "",
            strHomeNumber = "",
            strStreetAddress = "",
            strCity = "",
            strCountry = "",
            strAbout = "",
            strImagePath = "";
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        userImage = (ImageView) findViewById(R.id.user_image);
        addButton = (Button) findViewById(R.id.add_btn);
        firstName = (EditText) findViewById(R.id.first_name);
        lastName = (EditText) findViewById(R.id.last_name);
        mobileNumber = (EditText) findViewById(R.id.mobile_number);
        homeNumber = (EditText) findViewById(R.id.home_number);
        streetAddress = (EditText) findViewById(R.id.street);
        city = (EditText) findViewById(R.id.city);
        country = (EditText) findViewById(R.id.country);
        about = (EditText) findViewById(R.id.about);

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //select image
                int permissionCheck = ContextCompat.checkSelfPermission(AddContactActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
                if(permissionCheck == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(AddContactActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strFirstName = firstName.getText().toString();
                strLastName = lastName.getText().toString();
                strMobileNumber = mobileNumber.getText().toString();
                strHomeNumber = homeNumber.getText().toString();
                strStreetAddress = streetAddress.getText().toString();
                strCity = city.getText().toString();
                strCountry = country.getText().toString();
                strAbout = about.getText().toString();

                try {
                    if(!strFirstName.equals("") || !strLastName.equals("")) {
                        if(!strMobileNumber.equals("") || !strHomeNumber.equals("")) {
                            Person person = new Person(strFirstName,strLastName, strImagePath ,strStreetAddress,strCity,strCountry,strAbout);
                            PersonHelper personHelper = new PersonHelper(getApplicationContext());
                            int personId = personHelper.addPerson(person);
                            Log.d("personid", String.valueOf(personId));
                            if(personId > 0) {
                                person.setId(personId);

                                ContactHelper contactHelper = new ContactHelper(getApplicationContext());

                                if(!strMobileNumber.equals("")) {
                                    Contact mobileContact = new Contact(personId, strMobileNumber, "mobile");
                                    contactHelper.addContact(mobileContact);
                                }

                                if(!strHomeNumber.equals("")) {
                                    Contact homeContact = new Contact(personId, strHomeNumber, "home");
                                    contactHelper.addContact(homeContact);
                                }

                                firstName.setText("");
                                lastName.setText("");
                                userImage.setImageResource(R.drawable.user);
                                mobileNumber.setText("");
                                homeNumber.setText("");
                                streetAddress.setText("");
                                city.setText("");
                                country.setText("");
                                about.setText("");

                                Toast.makeText(getApplicationContext(), "Contact Added Successfully", Toast.LENGTH_LONG).show();
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if(requestCode == 1) {
                /*imageUri = data.getData();*/
                /*Log.d("userimage", String.valueOf(imageUri));*/

                imageUri = data.getData();
                userImage.setImageURI(imageUri);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            } else {
                Toast.makeText(getApplicationContext(), "Required External Storage Access Permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
