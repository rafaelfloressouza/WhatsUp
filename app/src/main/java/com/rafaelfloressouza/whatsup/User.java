package com.rafaelfloressouza.whatsup;

// Class used to represets a user with respective characteristics
public class User {

    private String name;
    private String phone;
    private String uid; // Unique id used to identify each user on firebase.
    private int image = -1; // TODO: Add the image functionality.

    public User(String uid, String name, String phone){
        this.name = name;
        this.phone = phone;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUid() {
        return uid;
    }
}
