package com.abstrack.hanasu.activity.welcome;

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
import com.abstrack.hanasu.activity.landing.LandingActivity;
import com.abstrack.hanasu.core.user.UserManager;
import com.abstrack.hanasu.util.AndroidUtil;
import com.abstrack.hanasu.core.Flame;
import com.abstrack.hanasu.util.TextUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

public class SetUsernameActivity extends BaseAppActivity {

    private EditText nameField, tagField;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_username);

        nameField = findViewById(R.id.editTextUsername); // editTextUsername
        tagField = findViewById(R.id.editTextTag); // editTextTag

        Button submitButton = findViewById(R.id.btnSubmit); // btnSubmit
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitUserIdentifier();
            }
        });

    }

    public void submitUserIdentifier() {
        String newIdentifier = nameField.getText().toString() + tagField.getText().toString();

        //Pending refactor
        if(!TextUtil.validateTextField(nameField, 20)){
            return;
        }

        if(!TextUtil.validateTextField(tagField, 4)){
            return;
        }

        createNewUser(newIdentifier);
    }

    public void createNewUser(String newIdentifier){
        Flame.getDataBaseReferenceWithPath("public").child("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                boolean identifierExisted = false;

                if(!task.isSuccessful()) {
                    Log.e("Hanasu-SetUsername", "Error getting data", task.getException());
                    return;
                }

                for(DataSnapshot data : task.getResult().getChildren()) {
                    String identifier = data.child("identifier").getValue(String.class);

                    if(identifier != null){
                        if(identifier.equals(newIdentifier)){
                            identifierExisted = true;
                            Toast.makeText(SetUsernameActivity.this, "Este nombre de usuario ya existe, puedes cambiar el nombre o id", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }

                if(!identifierExisted){
                    UserManager.writeNewUser(newIdentifier);
                    AndroidUtil.startNewActivity(SetUsernameActivity.this, SetProfileInfoActivity.class);
                }
            }
        });
    }
}
