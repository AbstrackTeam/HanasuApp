package com.abstrack.hanasu.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.core.user.UserManager;
import com.abstrack.hanasu.util.Preferences;
import com.abstrack.hanasu.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class SetUsernameActivity extends BaseAppActivity {

    public EditText nameField, tagField;
    public Button submitButton;
    public UserManager userManager;
    public String tag, identifier, name;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_username);

        name = "";
        identifier = "";

        nameField = findViewById(R.id.name);
        tagField = findViewById(R.id.tag);
        submitButton = findViewById(R.id.submit);

        userManager = new UserManager();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });

    }

    public void submit() {
        name = nameField.getText().toString();
        tag = tagField.getText().toString();
        identifier = name + tag;

        if(name.equals("")){
            Toast.makeText(this, "Ingresa el nombre de usuario", Toast.LENGTH_SHORT).show();
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

        DatabaseReference databaseReference = Util.getFbDatabase().getReference();
        databaseReference.child("users").child(identifier).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if(!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    return;
                }
                if(task.getResult().getValue() != null){
                    Toast.makeText(SetUsernameActivity.this, "Este usuario ya existe, puedes cambiar el nombre o la id", Toast.LENGTH_SHORT).show();
                }
                else{
                    Preferences.setIdentifier(identifier, SetUsernameActivity.this);
                    userManager.writeNewUser(name, tag);
                    Util.startNewActivity(SetUsernameActivity.this, LandingActivity.class);
                }
            }
        });
    }

}
