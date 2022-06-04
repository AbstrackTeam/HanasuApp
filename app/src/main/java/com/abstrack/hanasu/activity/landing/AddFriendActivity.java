package com.abstrack.hanasu.activity.landing;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.core.chatroom.ChatRoomManager;
import com.abstrack.hanasu.core.user.UserManager;
import com.abstrack.hanasu.core.Flame;;
import com.abstrack.hanasu.util.TextUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;


public class AddFriendActivity extends BaseAppActivity {

    private Button returnToLandingButton, addFriendButton;
    private EditText nameField, tagField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        init();
        buildButtonsListeners();
    }

    public void init(){
        nameField = findViewById(R.id.nameField);
        tagField = findViewById(R.id.tagField);

        returnToLandingButton = findViewById(R.id.returnToLandingButton);
        addFriendButton = findViewById(R.id.addFriend);
    }

    public void buildButtonsListeners() {
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFriend();
            }
        });

        returnToLandingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLastActivity();
            }
        });
    }

    public void addFriend() {
        String friendIdentifier = nameField.getText().toString() + tagField.getText().toString();

        if(!TextUtil.validateTextField(nameField, 20)){
            return;
        }

        if(!TextUtil.validateTextField(tagField, 4)){
            return;
        }

        if(UserManager.getCurrentPublicUser().getIdentifier().equals(friendIdentifier)) {
            Toast.makeText(AddFriendActivity.this, "No puedes agregarte a ti mismo.", Toast.LENGTH_SHORT).show();
            return;
        }

        for(String contactsIdentifier : UserManager.getCurrentPrivateUser().getContacts().keySet()){
            if(contactsIdentifier.equals(friendIdentifier)){
                Toast.makeText(AddFriendActivity.this, "Este usuario ya se encuentra en tu lista de contactos.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Flame.getDataBaseReferenceWithPath("public").child("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Log.e("Hanasu-AddFriend", "Error getting data", task.getException());
                    return;
                }

                for(DataSnapshot userData : task.getResult().getChildren()) {
                    if(userData.child("identifier").getValue(String.class) == null){
                        continue;
                    }

                    if(!userData.child("identifier").getValue(String.class).equals(friendIdentifier)){
                        continue;
                    }

                    String chatRoomUUID = UUID.randomUUID().toString();

                    UserManager.updateUserData("private", "contacts", retrieveNewContactList(friendIdentifier, chatRoomUUID));
                    UserManager.updateUserData("private", "chatRoomList", retrieveNewChatRoomList(chatRoomUUID));
                    ChatRoomManager.writeNewChatRoom(chatRoomUUID, friendIdentifier);

                    Toast.makeText(AddFriendActivity.this, "Has agregado a " + friendIdentifier + ".", Toast.LENGTH_SHORT).show();
                    goToLastActivity();
                    return;
                }

                Toast.makeText(AddFriendActivity.this, "El usuario no ha sido encontrado.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public HashMap<String, String> retrieveNewContactList(String friendIdentifier, String chatRoomUUID){
        HashMap<String, String> newContactsList = UserManager.getCurrentPrivateUser().getContacts();
        newContactsList.put(friendIdentifier, chatRoomUUID);
        return newContactsList;
    }

    public List<String> retrieveNewChatRoomList(String chatRoomUUID){
        List<String> newChatRoomList = UserManager.getCurrentPrivateUser().getChatRoomList();
        newChatRoomList.add(chatRoomUUID);
        return newChatRoomList;
    }

    public void goToLastActivity(){
        finish();
    }
}