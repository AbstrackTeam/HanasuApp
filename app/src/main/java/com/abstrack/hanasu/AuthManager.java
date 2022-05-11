package com.abstrack.hanasu;

import android.text.TextUtils;

import com.abstrack.hanasu.util.AndroidUtil;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

        if(!passwordText.matches(AndroidUtil.PASSWORD_PATTERN)){
            passwordTextInput.setError("Your password needs to include both lower and uppercase characters, and be at least 8 characters long.");
            return false;
        }

        passwordTextInput.setError(null);
        return true;
    }

    protected static boolean validateEmailText(TextInputLayout emailTextInput){
        String emailText = emailTextInput.getEditText().getText().toString();

        if(!validateTextField(emailTextInput)){
            return false;
        }

        if(!emailText.matches(AndroidUtil.EMAIL_PATTERN)){
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
