package com.sanatmondal.gasdistribution.model;

import java.io.Serializable;

public class ContactsModal implements Serializable {
    private String userName;
    private String contactNumber;

    public ContactsModal() {
    }

    public ContactsModal(String userName, String contactNumber) {
        this.userName = userName;
        this.contactNumber = contactNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
