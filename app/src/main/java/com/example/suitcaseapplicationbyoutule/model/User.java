package com.example.suitcaseapplicationbyoutule.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class User implements Parcelable {

    private String firstName;
    private String LastName;
    private String email;
    private String contact;
    private String password;

    public User() {

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(firstName);
        parcel.writeString(LastName);
        parcel.writeString(email);
        parcel.writeString(contact);
        parcel.writeString(password);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            User user = new User();
            user.setFirstName(in.readString());
            user.setLastName(in.readString());
            user.setEmail(in.readString());
            user.setContact(in.readString());
            user.setPassword(in.readString());
            return user;
        }
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }

    };

}

