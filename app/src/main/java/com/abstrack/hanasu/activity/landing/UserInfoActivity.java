package com.abstrack.hanasu.activity.landing;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.activity.welcome.SetProfileInfoActivity;
import com.abstrack.hanasu.core.user.UserManager;
import com.abstrack.hanasu.util.AndroidUtil;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserInfoActivity extends BaseAppActivity {

    TextView identifierNameTextView, identifierTagTextView, displayNameTextView;
    ImageView userIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        init();
    }

    private void init(){
        identifierNameTextView = findViewById(R.id.userIdentifierNameTextView);
        identifierTagTextView = findViewById(R.id.userIdentifierTagTextView);

        displayNameTextView = findViewById(R.id.displayNameTextView);

        userIcon = findViewById(R.id.profileUserIcon);

        Button returnToLanding = findViewById(R.id.goBackToLanding);
        returnToLanding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button editProfile = findViewById(R.id.editProfileInfoButton);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToProfileInfo();
            }
        });

        getAndDisplayInfo();
    }

    private void getAndDisplayInfo(){
        displayNameTextView.setText(UserManager.getCurrentUser().getDisplayName());
        identifierNameTextView.setText(UserManager.getCurrentUser().getName());
        identifierTagTextView.setText(UserManager.getCurrentUser().getTag());
        fetchProfilePicture();
    }

    private void fetchProfilePicture(){
        String imgKey = UserManager.getCurrentUser().getImgKey();
        String imgExtension = UserManager.getCurrentUser().getImgExtension();
        String imagePath = "image/" + imgKey + imgExtension;

        StorageReference imgRef = FirebaseStorage.getInstance().getReference();

        System.out.println(imagePath);

        imgRef.child(imagePath).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (!task.isSuccessful()) {
                    return;
                }

                System.out.println(task.getResult());

                if (imgExtension.equals(".gif")) {
                    Glide.with(UserInfoActivity.this).asGif().load(task.getResult()).into(userIcon);
                } else {
                    Glide.with(UserInfoActivity.this).asBitmap().load(task.getResult()).into(userIcon);
                }
            }
        });
    }

    private void goToProfileInfo(){
        AndroidUtil.startNewActivity(this, SetProfileInfoActivity.class);
    }
}