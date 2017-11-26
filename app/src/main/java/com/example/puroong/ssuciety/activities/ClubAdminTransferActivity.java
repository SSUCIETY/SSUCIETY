package com.example.puroong.ssuciety.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.puroong.ssuciety.R;
import com.example.puroong.ssuciety.activities.clublist.ClubListActivity;
import com.example.puroong.ssuciety.listeners.AfterQueryListener;
import com.example.puroong.ssuciety.api.UserAPI;
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contact = etContact.getText().toString();

                UserAPI.getInstance().getUserByContact(contact, getApplicationContext(), new AfterQueryListener() {
                    @Override
                    public void afterQuery(DataSnapshot dataSnapshot) {
                        // TODO: have to use datasnap getchildren
                        for(DataSnapshot clubScheduleSnapshot:dataSnapshot.getChildren()){
                            if(clubScheduleSnapshot.getValue(User.class) != null){
                                User newAdmin = new User(clubScheduleSnapshot);

                                newAdmin.setManagingClubId(user.getManagingClubId());
                                user.setManagingClubId(null);

                                UserAPI.getInstance().updateUser(newAdmin.getUid(), newAdmin);
                                UserAPI.getInstance().updateUser(user.getUid(), user);

                                Intent intent = new Intent(getApplicationContext(), ClubListActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "no such user", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
    }
}
