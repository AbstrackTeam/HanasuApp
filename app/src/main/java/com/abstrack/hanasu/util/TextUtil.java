package com.abstrack.hanasu.util;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$");

    public static boolean validateRegisterForm(TextInputLayout emailTextInput, TextInputLayout passwordTextInput, TextInputLayout confirmPasswordTextInput) {
        String passwordText = passwordTextInput.getEditText().getText().toString();
        String confirmPasswordText = confirmPasswordTextInput.getEditText().getText().toString();

        if (!validateEmailText(emailTextInput)) {
            return false;
        }

        if (!validatePasswordText(passwordTextInput)) {
            return false;
        }

        if (!validatePasswordText(confirmPasswordTextInput)) {
            return false;
        }

        if (!confirmPasswordText.equals(passwordText)) {
            confirmPasswordTextInput.setError("Password does not match.");
            return false;
        }

        return true;
    }

    public static boolean validateLoginForm(TextInputLayout emailTextInput, TextInputLayout passwordTextInput) {
        if (!validateEmailText(emailTextInput)) {
            return false;
        }

        if (!validatePasswordText(passwordTextInput)) {
            return false;
        }

        return true;
    }

    protected static boolean validatePasswordText(TextInputLayout passwordTextInput) {
        String passwordText = passwordTextInput.getEditText().getText().toString();

        if (!validateTextField(passwordTextInput)) {
            return false;
        }

        Matcher matcher = PASSWORD_PATTERN.matcher(passwordText);

        if (!matcher.matches()) {
            passwordTextInput.setError("Your password needs to include both lower and uppercase characters, and be at least 6 characters long.");
            return false;
        }

        passwordTextInput.setError(null);
        return true;
    }

    public static boolean validateEmailText(TextInputLayout emailTextInput) {
        String emailText = emailTextInput.getEditText().getText().toString();

        if (!validateTextField(emailTextInput)) {
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            emailTextInput.setError("Invalid email address.");
            return false;
        }

        emailTextInput.setError(null);
        return true;
    }

    public static boolean validateTextField(TextInputLayout textInput) {
        String textValue = textInput.getEditText().getText().toString();

        if (TextUtils.isEmpty(textValue)) {
            textInput.setError("Field cannot be left blank.");
            return false;
        }

        textInput.setError(null);
        return true;
    }

    public static boolean validateTextField(EditText editText) {
        String textValue = editText.getText().toString();

        if (TextUtils.isEmpty(textValue)) {
            editText.setError("Field cannot be left blank.");
            return false;
        }

        editText.setError(null);
        return true;
    }

    public static boolean validateTextField(EditText editText, int maxRange) {
        String textValue = editText.getText().toString();

        if (TextUtils.isEmpty(textValue)) {
            editText.setError("Field cannot be left blank.");
            return false;
        }

        if (textValue.length() > maxRange) {
            editText.setError("Este campo solo puede ser menor a " + maxRange + " letras.");
            return false;
        }

        editText.setError(null);
        return true;
    }
}
