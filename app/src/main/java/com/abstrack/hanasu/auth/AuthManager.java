package com.abstrack.hanasu.auth;

import android.text.TextUtils;
import android.util.Patterns;

import com.abstrack.hanasu.Util;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthManager {

    private static FirebaseAuth fireAuth = FirebaseAuth.getInstance();

    public static boolean validateRegisterForm(TextInputLayout emailTextInput, TextInputLayout passwordTextInput, TextInputLayout confirmPasswordTextInput) {
        String passwordText = passwordTextInput.getEditText().getText().toString();
        String confirmPasswordText = confirmPasswordTextInput.getEditText().getText().toString();

        if(!validateEmailText(emailTextInput)){
            return false;
        }

        if(!validatePasswordText(passwordTextInput)){
            return false;
        }

        if(!validatePasswordText(confirmPasswordTextInput)) {
            return false;
        }

        if(!confirmPasswordText.equals(passwordText)){
            confirmPasswordTextInput.setError("Password does not match.");
            return false;
        }

        return true;
    }

    public static boolean validateLoginForm(TextInputLayout emailTextInput, TextInputLayout passwordTextInput) {
        if(!validateEmailText(emailTextInput)){
            return false;
        }

        if(!validatePasswordText(passwordTextInput)){
            return false;
        }

        return true;
    }

    protected static boolean validatePasswordText(TextInputLayout passwordTextInput){
        String passwordText = passwordTextInput.getEditText().getText().toString();

        if(!validateTextField(passwordTextInput)){
            return false;
        }

        Matcher matcher = Util.PASSWORD_PATTERN.matcher(passwordText);

        if(!matcher.matches()){
            passwordTextInput.setError("Your password needs to include both lower and uppercase characters, and be at least 6 characters long.");
            return false;
        }

        passwordTextInput.setError(null);
        return true;
    }

    public static boolean validateEmailText(TextInputLayout emailTextInput){
        String emailText = emailTextInput.getEditText().getText().toString();

        if(!validateTextField(emailTextInput)){
            return false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            emailTextInput.setError("Invalid email address.");
            return false;
        }

        emailTextInput.setError(null);
        return true;
    }

    protected static boolean validateTextField(TextInputLayout textInput){
        String textValue = textInput.getEditText().getText().toString();

        if (TextUtils.isEmpty(textValue)) {
            textInput.setError("Field cannot be left blank.");
            return false;
        }

        textInput.setError(null);
        return true;
    }

    public static boolean isUserLogged() {
        FirebaseUser currentUser = fireAuth.getCurrentUser();

        if(currentUser == null)
            return false;

        return true;
    }

    public static FirebaseAuth getFireAuth() {
        return fireAuth;
    }
}
