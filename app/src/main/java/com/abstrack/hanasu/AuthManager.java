package com.abstrack.hanasu;

import android.text.TextUtils;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthManager {

    private static FirebaseAuth fireAuth = FirebaseAuth.getInstance();

    public static boolean validateRegisterForm(TextInputLayout emailTextInput, TextInputLayout passwordTextInput, TextInputLayout confirmPasswordTextInput) {
        String passwordText = passwordTextInput.getEditText().getText().toString();
        String confirmPasswordText = confirmPasswordTextInput.getEditText().getText().toString();

        if(!validateTextField(emailTextInput)) {
            return false;
        }

        if(!validateTextField(passwordTextInput)) {
            return false;
        }

        if(!validateTextField(confirmPasswordTextInput)) {
            return false;
        }

        if(!confirmPasswordText.equals(passwordText)){
            confirmPasswordTextInput.getEditText().setError("Password does not match.");
            return false;
        }

        return true;
    }

    public static boolean validateLoginForm(TextInputLayout emailTextInput, TextInputLayout passwordTextInput) {
        if(!validateTextField(emailTextInput)){
            return false;
        }

        if(!validateTextField(passwordTextInput)){
            return false;
        }

        return true;
    }

    public static boolean validateTextField(TextInputLayout textInput){
        String textValue = textInput.getEditText().getText().toString();

        if (TextUtils.isEmpty(textValue)) {
            textInput.setError("Required");
            return false;
        }

        textInput.getEditText().setError(null);
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
