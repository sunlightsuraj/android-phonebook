package model;


import android.net.Uri;

/**
 * Created by suraj on 1/16/16.
 * Email suraj.sht@hotmail.com
 */
public class Person {
    private int id;
    private Uri image;
    private String firstName = "",lastName = "",street = "",city = "",country = "",about = "";
    private String imagePath = "";

    public Person() {}

    public Person(String firstName, String lastName, String imagePath, String street, String city, String country, String about) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.imagePath = imagePath;
        this.street = street;
        this.city = city;
        this.country = country;
        this.about = about;
    }

    public Person(int id, String firstName, String lastName, String imagePath, String street, String city, String country, String about) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imagePath = imagePath;
        this.street = street;
        this.city = city;
        this.country = country;
        this.about = about;
    }

    public int getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getAbout() {
        return about;
    }

    public Uri getImage() {
        return image;
    }

    /**==============================================**/

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
