package com.example.puroong.ssuciety.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
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
import com.example.puroong.ssuciety.activities.manageclub.ManageClubActivity;
import com.example.puroong.ssuciety.listeners.AfterImageLoadListener;
import com.example.puroong.ssuciety.listeners.AfterImageUploadListener;
import com.example.puroong.ssuciety.listeners.AfterQueryListener;
import com.example.puroong.ssuciety.api.ClubAPI;
import com.example.puroong.ssuciety.api.UserAPI;
import com.example.puroong.ssuciety.models.Club;
import com.example.puroong.ssuciety.models.User;
import com.example.puroong.ssuciety.utils.ImageUtil;
import com.google.firebase.database.DataSnapshot;

import java.io.IOException;
import java.util.Arrays;

public class ClubUpdateActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    private ImageView clubWallpaper;
    private Uri clubWallpaperUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_form);

        User user = UserAPI.getInstance().getCurrentUser();
        String clubId = user.getManagingClubId();

        ClubAPI.getInstance().getClubByKey(clubId, getApplicationContext(), new AfterQueryListener() {
            @Override
            public void afterQuery(DataSnapshot dataSnapshot) {
                final Club club = new Club(dataSnapshot);

                clubWallpaper = (ImageView) findViewById(R.id.ivClubWallpaper);
                final TextView clubName = (TextView) findViewById(R.id.tvClubName);
                final TextView clubIntro = (TextView) findViewById(R.id.tvClubIntro);
                final TextView clubDescription = (TextView) findViewById(R.id.tvClubDescription);
                final Switch clubHasRoom = (Switch) findViewById(R.id.swHasRoom);
                final Spinner clubTagType = (Spinner) findViewById(R.id.spTagType);

                // load image
                ImageUtil.getInstance().loadImage(getApplicationContext(), handler, club.getWallpaperLink(), new AfterImageLoadListener() {
                    @Override
                    public void setImage(Bitmap bitmap) {
                        ImageUtil.getInstance().setImage(clubWallpaper, bitmap, true);
                    }
                });

                // set the rest
                clubName.setText(club.getName());
                clubIntro.setText(club.getIntroduction());
                clubDescription.setText(club.getDescription());
                clubHasRoom.setChecked(club.isHasClubroom());

                String[] tags = getResources().getStringArray(R.array.tag_array);
                int index = Arrays.asList(tags).indexOf(club.getTagId());
                clubTagType.setSelection(index);

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
                                    String intro = clubIntro.getText().toString();
                                    String description = clubDescription.getText().toString();
                                    boolean hasRoom = clubHasRoom.isChecked();
                                    String tagName = clubTagType.getSelectedItem().toString();

                                    club.setWallpaperLink(downloadUrl);
                                    club.setName(name);
                                    club.setIntroduction(intro);
                                    club.setDescription(description);
                                    club.setHasClubroom(hasRoom);
                                    club.setTagId(tagName);

                                    ClubAPI.getInstance().updateClub(club.getKey(), club);

                                    startActivity(new Intent(getApplicationContext(), ManageClubActivity.class));
                                    finish();
                                }
                            });
                        } else {
                            String name = clubName.getText().toString();
                            String intro = clubIntro.getText().toString();
                            String description = clubDescription.getText().toString();
                            boolean hasRoom = clubHasRoom.isChecked();
                            String tagName = clubTagType.getSelectedItem().toString();

                            club.setName(name);
                            club.setIntroduction(intro);
                            club.setDescription(description);
                            club.setHasClubroom(hasRoom);
                            club.setTagId(tagName);

                            ClubAPI.getInstance().updateClub(club.getKey(), club);

                            startActivity(new Intent(getApplicationContext(), ManageClubActivity.class));
                            finish();
                        }

                    }
                });

                // choose image
                clubWallpaper.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageUtil.getInstance().chooseImage(ClubUpdateActivity.this);
                    }
                });
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
