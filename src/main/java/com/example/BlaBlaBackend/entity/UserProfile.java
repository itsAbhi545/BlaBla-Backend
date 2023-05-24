package com.example.BlaBlaBackend.entity;

import com.example.BlaBlaBackend.util.Regex;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
@Entity
@Table(name = "userprofile")
public class UserProfile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    //userImg,dob,
    @OneToOne
    @JoinColumn(name = "user")
    private User user;
    @NotNull(message = "FirstName can't be null")
    private String firstName;
    @NotNull(message = "LastName can't be null")
    private String lastName;
    private String userImageUrl;
    @NotNull(message = "D.O.B can't be null")
    private String dob;
    @Pattern(regexp = Regex.PHONENUMBER,message = "Enter valid Phone-Number")
    private String phoneNumber;
    private String gender = "MALE";

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public String getDob() {
        return dob;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser(){
        return this.user;
    }
}
