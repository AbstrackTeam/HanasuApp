package com.abstrack.hanasu.core.user;

import android.util.Log;

import androidx.annotation.NonNull;

import com.abstrack.hanasu.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class UserManager {

    private final DatabaseReference databaseReference;

    public UserManager() {
        String PATH = "users";
        databaseReference = Util.getDatabaseReference().getReference(PATH);
    }

    public void writeNewUser(String uid) {
        User user = new User(uid);
        databaseReference.child(uid).setValue(user);
    }

    public void updateUser(String uid, User user){
        databaseReference.child(uid).setValue(user);
    }

    public boolean userOnDatabase(String uid){

        boolean[] isOnDatabase = new boolean[1];

        databaseReference.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                if(task.getResult().getValue() != null){
                    isOnDatabase[0] = true;
                }

            }
        });

        return isOnDatabase[0];
    }
}
