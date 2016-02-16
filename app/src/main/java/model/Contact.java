package model;

/**
 * Created by suraj on 1/16/16.
 * Email suraj.sht@hotmail.com
 */
public class Contact {
    private int id;
    private int personId;
    private String contactNumber = "", contactType = "";

    public Contact() {}

    public Contact(int personId, String contactNumber, String contactType) {
        this.personId = personId;
        this.contactNumber = contactNumber;
        this.contactType = contactType;
    }

    public int getPersonId() {
        return personId;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getContactType() {
        return contactType;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
