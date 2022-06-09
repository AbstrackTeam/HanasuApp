package com.abstrack.hanasu.core.contact;

import android.util.Log;

import androidx.annotation.NonNull;

import com.abstrack.hanasu.callback.OnUserDataReceiveCallback;
import com.abstrack.hanasu.core.Flame;
import com.abstrack.hanasu.core.user.PublicUser;
import com.abstrack.hanasu.core.user.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;

public class ContactManager {

    private static HashMap<String, PublicUser> contactPublicUserList = new HashMap<String, PublicUser>();

    public static void fetchPublicData(OnUserDataReceiveCallback userDataReceiveCallback) {
        int listIndex = 0;
        contactPublicUserList.clear();

        if (Flame.getFireAuth().getCurrentUser() != null) {
            for (String contactIdentifier : UserManager.currentPrivateUser.getContacts().keySet()){
                listIndex++;
                int finalListIndex = listIndex;

                if(!contactIdentifier.equals("friendIdentifier")) {
                    Flame.getDataBaseReferenceWithPath("public").child("users").orderByChild("identifier").equalTo(contactIdentifier).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.d("Hanasu-ContactManager", "(PublicUser) Error getting data");
                                return;
                            }

                            PublicUser contactPublicUser = task.getResult().getValue(PublicUser.class);

                            if (contactPublicUser != null) {
                                addPublicUserToContactList(contactPublicUser);
                                userDataReceiveCallback.onDataReceiver(contactPublicUser);

                                if(finalListIndex == UserManager.currentPrivateUser.getContacts().keySet().size() - 1) {
                                    Log.d("Hanasu-ContactManager", "(PublicUser) Data fetched");
                                    userDataReceiveCallback.onDataReceived();
                                }
                            }
                        }
                    });
                }
            }
        }
    }

    public static HashMap<String, PublicUser> getContactPublicUserList(){
        return contactPublicUserList;
    }

    public static void addPublicUserToContactList(PublicUser contactPublicUser){
        if(!contactPublicUserList.containsKey(contactPublicUser.getIdentifier())) {
            contactPublicUserList.put(contactPublicUser.getIdentifier(), contactPublicUser);
        }
    }

}
