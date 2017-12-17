package com.example.puroong.ssuciety.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.puroong.ssuciety.R;
import com.example.puroong.ssuciety.activities.clublist.ClubListActivity;
import com.example.puroong.ssuciety.api.ClubAPI;
import com.example.puroong.ssuciety.listeners.AfterQueryListener;
import com.example.puroong.ssuciety.api.UserAPI;
import com.example.puroong.ssuciety.models.Club;
import com.example.puroong.ssuciety.models.User;
import com.google.firebase.database.DataSnapshot;

public class ClubAdminTransferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_admin_transfer);

        final User user = UserAPI.getInstance().getCurrentUser();

        final EditText etContact = (EditText) findViewById(R.id.etContact);
        final Button submit = (Button) findViewById(R.id.btnSubmit);

        final String clubId = UserAPI.getInstance().getCurrentUser().getManagingClubId();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contact = etContact.getText().toString();

                UserAPI.getInstance().getUserByContact(contact, getApplicationContext(), new AfterQueryListener() {
                    @Override
                    public void afterQuery(DataSnapshot dataSnapshot) {
                        int len = 0;

                        for(final DataSnapshot userDataSnapshot:dataSnapshot.getChildren()){
                            len++;

                            if(userDataSnapshot.getValue(User.class) != null){

                                ClubAPI.getInstance().getClubByKey(clubId, getApplicationContext(), new AfterQueryListener() {
                                    @Override
                                    public void afterQuery(DataSnapshot dataSnapshot) {
                                        Club club = new Club(dataSnapshot);
                                        User newAdmin = new User(userDataSnapshot);

                                        // update user managing club info
                                        newAdmin.setManagingClubId(user.getManagingClubId());
                                        user.setManagingClubId(null);

                                        // update club admin info
                                        club.setAdminId(newAdmin.getUid());

                                        UserAPI.getInstance().updateUser(newAdmin.getUid(), newAdmin);
                                        UserAPI.getInstance().updateUser(user.getUid(), user);

                                        ClubAPI.getInstance().updateClub(clubId, club);

                                        Intent intent = new Intent(getApplicationContext(), ClubListActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "no such user", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if(len == 0){
                            Toast.makeText(getApplicationContext(), "no such user", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
