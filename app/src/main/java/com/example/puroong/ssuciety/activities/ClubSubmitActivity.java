package com.example.puroong.ssuciety.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.puroong.ssuciety.R;
import com.example.puroong.ssuciety.activities.clublist.ClubListActivity;
import com.example.puroong.ssuciety.activities.manageclub.ManageClubActivity;
import com.example.puroong.ssuciety.api.ClubAPI;
import com.example.puroong.ssuciety.api.UserAPI;
import com.example.puroong.ssuciety.listeners.AfterImageUploadListener;
import com.example.puroong.ssuciety.models.Club;
import com.example.puroong.ssuciety.utils.ImageUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

public class ClubSubmitActivity extends AppCompatActivity {

    private Uri clubWallpaperUrl;
    private ImageView clubWallpaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_form);


        final TextView clubName = (TextView) findViewById(R.id.tvClubName);
        final TextView clubIntro = (TextView) findViewById(R.id.tvClubIntro);
        final TextView clubDescription = (TextView) findViewById(R.id.tvClubDescription);
        final Switch clubHasRoom = (Switch) findViewById(R.id.swHasRoom);
        final Spinner clubTagType = (Spinner) findViewById(R.id.spTagType);
        clubWallpaper = (ImageView) findViewById(R.id.ivClubWallpaper);

        Button submit = (Button) findViewById(R.id.btnSubmit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable) clubWallpaper.getDrawable()).getBitmap();
                String url = null;

                if(clubWallpaperUrl != null){
                    url = clubWallpaperUrl.toString();

                    ImageUtil.getInstance().uploadImage(getApplicationContext(), bitmap, url, new AfterImageUploadListener() {
                        @Override
                        public void afterImageUpload(String downloadUrl) {
                            String name = clubName.getText().toString();
                            String wallpaperLink = downloadUrl;
                            String intro = clubIntro.getText().toString();
                            String description = clubDescription.getText().toString();
                            boolean hasRoom = clubHasRoom.isChecked();
                            String tagName = clubTagType.getSelectedItem().toString();
                            String adminId = UserAPI.getInstance().getCurrentUser().getUid();

                            Club club = new Club(name, wallpaperLink, intro, description, hasRoom, tagName, adminId, null, null, null);
                            ClubAPI.getInstance().registerClub(club);

                            startActivity(new Intent(getApplicationContext(), ClubListActivity.class));
                            finish();
                        }
                    });
                } else {
                    String name = clubName.getText().toString();
                    String wallpaperLink = "";
                    String intro = clubIntro.getText().toString();
                    String description = clubDescription.getText().toString();
                    boolean hasRoom = clubHasRoom.isChecked();
                    String tagName = clubTagType.getSelectedItem().toString();
                    String adminId = UserAPI.getInstance().getCurrentUser().getUid();

                    Club club = new Club(name, wallpaperLink, intro, description, hasRoom, tagName, adminId, null, null, null);
                    ClubAPI.getInstance().registerClub(club);

                    startActivity(new Intent(getApplicationContext(), ManageClubActivity.class));
                    finish();
                }
            }
        });

        // choose image
        clubWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUtil.getInstance().chooseImage(ClubSubmitActivity.this);
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

                ImageUtil.getInstance().setImage(clubWallpaper, bitmap, true);
                clubWallpaperUrl = uri;
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Failed to get image from gallery", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
