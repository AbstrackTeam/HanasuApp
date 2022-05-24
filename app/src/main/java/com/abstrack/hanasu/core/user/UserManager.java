package com.abstrack.hanasu.core.user;

import com.abstrack.hanasu.db.FireDB;
import com.google.firebase.database.DatabaseReference;
import java.util.Random;

public class UserManager {

    private static User currentUser;

    public static void createCurrentUser(String name, String tag) {
        currentUser = new User(name, tag);
        writeNewUser();
    }

    public static void writeNewUser(){
        FireDB.getDataBaseReferenceWithPath("users").child(currentUser.getIdentifier()).setValue(UserManager.getCurrentUser());
    }

    public static void uploadUserData(String path, String value) {
        FireDB.getDataBaseReferenceWithPath("users").child(currentUser.getIdentifier()).child(path).setValue(value);
    }

    public static User getCurrentUser(){
        return currentUser;
    }
}
