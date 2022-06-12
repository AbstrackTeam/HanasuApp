package com.abstrack.hanasu.activity.landing;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.core.Flame;
import com.abstrack.hanasu.core.chatroom.ChatRoomManager;
import com.abstrack.hanasu.core.chatroom.data.ChatType;
import com.abstrack.hanasu.core.user.UserManager;
import com.abstrack.hanasu.util.AndroidUtil;
import com.abstrack.hanasu.util.TextUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.UUID;

;


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

    public void init() {
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
                goToLandingActivity();
            }
        });
    }

    public void addFriend() {
        String friendIdentifier = nameField.getText().toString() + tagField.getText().toString();

        if (!TextUtil.validateTextField(nameField, 20)) {
            return;
        }

        if (!TextUtil.validateTextField(tagField, 4)) {
            return;
        }

        if (UserManager.currentPublicUser.getIdentifier().equals(friendIdentifier)) {
            Toast.makeText(AddFriendActivity.this, "No puedes agregarte a ti mismo.", Toast.LENGTH_SHORT).show();
            return;
        }

        for (String contactsIdentifier : UserManager.currentPrivateUser.getContacts().keySet()) {
            if (contactsIdentifier.equals(friendIdentifier)) {
                Toast.makeText(AddFriendActivity.this, "Este usuario ya se encuentra en tu lista de contactos.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Flame.getDataBaseReferenceWithPath("public").child("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("Hanasu-AddFriend", "Error getting data", task.getException());
                    return;
                }

                for (DataSnapshot userData : task.getResult().getChildren()) {
                    if (userData.child("identifier").getValue(String.class) == null) {
                        continue;
                    }

                    if (!userData.child("identifier").getValue(String.class).equals(friendIdentifier)) {
                        continue;
                    }

                    String chatRoomUUID = UUID.randomUUID().toString();

                    UserManager.addNewContact(chatRoomUUID, friendIdentifier);
                    UserManager.sendFriendRequest(chatRoomUUID, friendIdentifier);
                    ChatRoomManager.writeNewIndividualChatRoom(chatRoomUUID, ChatType.INDIVIDUAL, friendIdentifier);

                    Toast.makeText(AddFriendActivity.this, "Has agregado a " + friendIdentifier + ".", Toast.LENGTH_SHORT).show();
                    goToLandingActivity();
                    return;
                }

                Toast.makeText(AddFriendActivity.this, "El usuario no ha sido encontrado.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goToLandingActivity() {
        AndroidUtil.startNewActivity(this, LandingActivity.class);
    }
}