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
import com.abstrack.hanasu.core.user.UserManager;
import com.abstrack.hanasu.db.FireDatabase;;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;


public class AddFriendActivity extends BaseAppActivity {

    Button returnToLandingButton, addFriendButton;
    EditText nameField, tagField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        init();
    }

    public void init(){
        nameField = findViewById(R.id.nameField);
        tagField = findViewById(R.id.tagField);

        returnToLandingButton = findViewById(R.id.returnToLandingButton);
        addFriendButton = findViewById(R.id.addFriend);

        buildButtonsListeners();
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
                finish();
            }
        });
    }

    public void addFriend() {
        String name = nameField.getText().toString();
        String tag = tagField.getText().toString();
        String identifier = name + tag;

        String ownIdentifier = UserManager.getCurrentUser().getIdentifier();

        FireDatabase.getDataBaseReferenceWithPath("users").child(identifier).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Log.e("firebase", "Error getting data", task.getException());
                    return;
                }

                if(task.getResult().getValue() != null){
                    if(task.getResult().child("identifier").getValue().toString().equals(ownIdentifier)){
                        Toast.makeText(AddFriendActivity.this, "No puedes agregarte a ti mismo XD", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    UserManager.addToUserContacts(identifier);
                    return;
                }
                Toast.makeText(AddFriendActivity.this, "El usuarii no ha sido encontrado", Toast.LENGTH_SHORT).show();
            }
        });
    }
}