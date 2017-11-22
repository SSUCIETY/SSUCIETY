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
import com.example.puroong.ssuciety.api.ClubAPI;
import com.example.puroong.ssuciety.models.Club;
import com.google.firebase.auth.FirebaseAuth;

public class ClubSubmitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_form);

        Button submit = (Button) findViewById(R.id.btnSubmit);

        final TextView clubName = (TextView) findViewById(R.id.tvClubName);
        final TextView clubIntro = (TextView) findViewById(R.id.tvClubIntro);
        final TextView clubDescription = (TextView) findViewById(R.id.tvClubDescription);
        final Switch clubHasRoom = (Switch) findViewById(R.id.swHasRoom);
        final Spinner clubTagType = (Spinner) findViewById(R.id.spTagType);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = clubName.getText().toString();
                String intro = clubIntro.getText().toString();
                String description = clubDescription.getText().toString();
                boolean hasRoom = clubHasRoom.isChecked();
                String tagName = clubTagType.getSelectedItem().toString();
                String adminId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Club club = new Club(name, "wallpaperlink", intro, description, hasRoom, tagName, adminId, null, null, null);
                ClubAPI.getInstance().registerClub(club);

                startActivity(new Intent(getApplicationContext(), ClubListActivity.class));
            }
        });
    }
}
