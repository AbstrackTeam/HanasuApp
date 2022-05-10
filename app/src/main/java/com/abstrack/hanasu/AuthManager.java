package com.abstrack.hanasu;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthManager {

    private static FirebaseAuth fireAuth = FirebaseAuth.getInstance();

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
