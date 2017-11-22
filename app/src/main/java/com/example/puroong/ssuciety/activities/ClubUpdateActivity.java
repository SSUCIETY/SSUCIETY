package com.example.puroong.ssuciety.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.puroong.ssuciety.R;
import com.example.puroong.ssuciety.activities.clublist.ClubListActivity;
import com.example.puroong.ssuciety.activities.manageclub.ManageClubActivity;
import com.example.puroong.ssuciety.api.AfterQueryListener;
import com.example.puroong.ssuciety.api.ClubAPI;
import com.example.puroong.ssuciety.api.UserAPI;
import com.example.puroong.ssuciety.models.Club;
import com.example.puroong.ssuciety.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.Arrays;

public class ClubUpdateActivity extends AppCompatActivity {

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

                final TextView clubName = (TextView) findViewById(R.id.tvClubName);
                final TextView clubIntro = (TextView) findViewById(R.id.tvClubIntro);
                final TextView clubDescription = (TextView) findViewById(R.id.tvClubDescription);
                final Switch clubHasRoom = (Switch) findViewById(R.id.swHasRoom);
                final Spinner clubTagType = (Spinner) findViewById(R.id.spTagType);

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
                });
            }
        });
    }
}
