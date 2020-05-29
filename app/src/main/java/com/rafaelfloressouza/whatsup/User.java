package com.rafaelfloressouza.whatsup;

// Class used to represed a user with respective characteristics
public class User {

    private String name;
    private String phone;
    private int image = -1; // TODO: Add the image functionality.

    public User(String name, String phone){
        this.name = name;
        this.phone = phone;
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
}
