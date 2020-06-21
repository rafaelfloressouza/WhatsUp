package com.rafaelfloressouza.whatsup.Objects;

import android.os.Parcel;
import android.os.Parcelable;

// Class used to represets a user with respective characteristics
public class User implements Parcelable {

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

    protected User(Parcel in) {
        name = in.readString();
        phone = in.readString();
        uid = in.readString();
        image = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(uid);
        dest.writeInt(image);
    }
}
