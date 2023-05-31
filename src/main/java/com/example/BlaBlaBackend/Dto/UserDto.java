package com.example.BlaBlaBackend.Dto;

import com.example.BlaBlaBackend.customJsonDeserializer.Trim;
import com.example.BlaBlaBackend.util.Regex;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto{
    @NotEmpty(message = "FirstName can't be null")
    @Trim
    private String firstName;
    @NotEmpty(message = "LastName can't be null")
    @Trim
    private String lastName;
    @NotEmpty(message = "Email can't be null")
    @Pattern(regexp = Regex.EMAIL,message = "Enter valid email")
    @Trim
    private String email;
    @NotEmpty(message = "Password can't be null")
    @Pattern(regexp = Regex.PASSWORD,message = "Enter valid Password")
    @Trim
    private String password;
    @Trim
    private String gender="MALE";
    @NotEmpty(message = "DateOfBirth can't be null")
    @Trim
    private  String dob;
    @Pattern(regexp = Regex.PHONENUMBER)
    @Trim
    private String phoneNumber;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
