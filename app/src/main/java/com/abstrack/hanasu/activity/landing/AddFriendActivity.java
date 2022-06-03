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
import com.abstrack.hanasu.core.Flame;;
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
        // String ownIdentifier = UserManager.getCurrentUser().getIdentifier();

        if(name.length() == 0){
            Toast.makeText(this, "Ingresa un nombre", Toast.LENGTH_SHORT).show();
            return;
        }

        if(name.length() > 20){
            Toast.makeText(this, "Solo nombres menores a 20 letras", Toast.LENGTH_SHORT).show();
            return;
        }

        if(tag.length() != 4 ){
            Toast.makeText(this, "Las tags solo pueden ser de 4 digitos", Toast.LENGTH_SHORT).show();
            return;
        }

        Flame.getDataBaseReferenceWithPath("users").child(identifier).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Log.e("firebase", "Error getting data", task.getException());
                    return;
                }

                if(task.getResult().getValue() != null){
                 /**   if(task.getResult().child("identifier").getValue().toString().equals(ownIdentifier)){
                        Toast.makeText(AddFriendActivity.this, "No puedes agregarte a ti mismo XD", Toast.LENGTH_SHORT).show();
                        return;
                    } **/
                   // UserManager.addToUserContacts(identifier);
                    Toast.makeText(AddFriendActivity.this, "Has agregado a " + name, Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(AddFriendActivity.this, "El usuarii no ha sido encontrado", Toast.LENGTH_SHORT).show();
            }
        });
    }
}