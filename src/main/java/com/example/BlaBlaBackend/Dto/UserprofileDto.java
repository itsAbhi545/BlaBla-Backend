package com.example.BlaBlaBackend.Dto;

import com.example.BlaBlaBackend.customAnnotation.Trim;
import com.example.BlaBlaBackend.customAnnotation.TrimConverterRequestParam;
import com.example.BlaBlaBackend.util.Regex;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class UserprofileDto {
    @NotBlank(message = "FirstName can't be null")
    @Trim
    private String firstName;
    @NotNull(message = "LastName can't be null")
    @Trim
    private String lastName;
    @Trim
    private String userImageUrl;
    @Trim
    @NotNull(message = "D.O.B can't be null")
    private String dob;
    @Convert(converter = TrimConverterRequestParam.class)
    @Pattern(regexp = Regex.PHONENUMBER,message = "Enter valid Phone-Number")
    private String phoneNumber;
    @Trim
    private String gender = "MALE";

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

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
