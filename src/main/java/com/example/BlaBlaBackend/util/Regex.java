package com.example.BlaBlaBackend.util;

public class Regex {
    public final static String EMAIL = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
    public final static String PASSWORD = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
    public boolean matchEmail(String email){
        return email.matches(EMAIL);
    }
    public boolean matchPassword(String password){
        return password.matches(PASSWORD);
    }
}
