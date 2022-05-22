package com.abstrack.hanasu.core.user;

import com.abstrack.hanasu.util.Util;
import com.google.firebase.database.DatabaseReference;
import java.util.Random;

public class UserManager {

    private final DatabaseReference databaseReference;

    public UserManager() {
        String PATH = "users";
        databaseReference = Util.getFbDatabase().getReference(PATH);
    }

    public String generateTag() {
        Random random = new Random();

        String tag = String.format("%04d", random.nextInt(10000));

        return tag;
    }

    public void writeNewUser(String name, String tag) {
        User user = new User(name, tag);
        databaseReference.child(user.getIdentifier()).setValue(user);
    }

}
