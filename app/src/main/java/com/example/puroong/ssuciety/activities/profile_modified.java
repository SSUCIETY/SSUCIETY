package com.example.puroong.ssuciety.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.puroong.ssuciety.R;
import com.example.puroong.ssuciety.activities.clublist.ClubListActivity;
import com.example.puroong.ssuciety.api.UserAPI;
import com.example.puroong.ssuciety.listeners.AfterImageLoadListener;
import com.example.puroong.ssuciety.listeners.AfterImageUploadListener;
import com.example.puroong.ssuciety.models.User;
import com.example.puroong.ssuciety.utils.ImageUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.ArrayList;

public class profile_modified extends AppCompatActivity {
    Handler handler = new Handler();
    ImageView imageView;
    Uri imageViewUrl;

    EditText edName;
    EditText edMajor;
    EditText edYear;
    EditText edContact;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_modified);

        edName = (EditText) findViewById(R.id.edName);
        edMajor = (EditText) findViewById(R.id.edMajor);
        edYear = (EditText) findViewById(R.id.edYear);
        edContact = (EditText) findViewById(R.id.edContact);
        imageView = (ImageView) findViewById(R.id.imageView);
        button = (Button) findViewById(R.id.button);

        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

        imageViewUrl = fUser.getPhotoUrl();
        ImageUtil.getInstance().loadImage(getApplicationContext(), handler, imageViewUrl.toString(), new AfterImageLoadListener() {
            @Override
            public void setImage(Bitmap bitmap) {
                ImageUtil.getInstance().setImage(imageView, bitmap, true);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                String url = null;

                if(imageViewUrl != null){
                    url = imageViewUrl.toString();

                    ImageUtil.getInstance().uploadImage(getApplicationContext(), bitmap, url, new AfterImageUploadListener() {
                        @Override
                        public void afterImageUpload(String downloadUrl) {
                            String name = edName.getText().toString();
                            String email = fUser.getEmail();
                            String phoneNumber = edContact.getText().toString();
                            String profileLink = downloadUrl;
                            String major = edMajor.getText().toString();
                            int admissionYear = Integer.parseInt(edYear.getText().toString());
                            int gender = 1;

                            User newUser = new User(name, email, phoneNumber, profileLink, major, admissionYear, gender, null, null);

                            String uid = fUser.getUid();

                            UserAPI.getInstance().registerUser(uid, newUser);
                            startActivity(new Intent(getApplicationContext(), ClubListActivity.class));
                            finish();
                        }
                    });
                } else {
                    String name = edName.getText().toString();
                    String email = fUser.getEmail();
                    String phoneNumber = edContact.getText().toString();
                    String profileLink = "";
                    String major = edMajor.getText().toString();
                    int admissionYear = Integer.parseInt(edYear.getText().toString());
                    int gender = 1;

                    User newUser = new User(name, email, phoneNumber, profileLink, major, admissionYear, gender, null, null);

                    String uid = fUser.getUid();

                    UserAPI.getInstance().registerUser(uid, newUser);
                    startActivity(new Intent(getApplicationContext(), ClubListActivity.class));
                    finish();
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUtil.getInstance().chooseImage(profile_modified.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ImageUtil.PICK_IMAGE && resultCode == RESULT_OK) {
            try {
                Uri uri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                ImageUtil.getInstance().setImage(imageView, bitmap, true);
                imageViewUrl = uri;
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Failed to get image from gallery", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
