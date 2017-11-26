package com.example.puroong.ssuciety.activities.clubschedulemanage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.puroong.ssuciety.R;
import com.example.puroong.ssuciety.activities.ClubScheduleSubmitActivity;
import com.example.puroong.ssuciety.activities.ClubScheduleUpdateActivity;
import com.example.puroong.ssuciety.api.ClubScheduleAPI;
import com.example.puroong.ssuciety.api.UserAPI;
import com.example.puroong.ssuciety.models.ClubSchedule;
import com.example.puroong.ssuciety.models.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ClubScheduleManageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_schedule_manage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ClubScheduleSubmitActivity.class);
                startActivity(intent);
            }
        });
        // set variables
        User user = UserAPI.getInstance().getCurrentUser();
        final String clubId = user.getManagingClubId();

        ListView listView = (ListView) findViewById(R.id.lvClubScheduleList);
        final ClubScheduleManageAdapter adapter = new ClubScheduleManageAdapter(getApplicationContext(), new ArrayList<ClubSchedule>());

        listView.setAdapter(adapter);
        // set child event listener
        DatabaseReference clubScheduleRef = FirebaseDatabase.getInstance().getReference().child(ClubScheduleAPI.databaseName);

        clubScheduleRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ClubSchedule clubSchedule = new ClubSchedule(dataSnapshot);

                if(clubId.equals(clubSchedule.getClubId())){
                    adapter.add(clubSchedule);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                ClubSchedule clubSchedule = new ClubSchedule(dataSnapshot);

                if(clubId.equals(clubSchedule.getClubId())){
                    adapter.updateByKey(clubSchedule.getKey(), clubSchedule);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                ClubSchedule clubSchedule = new ClubSchedule(dataSnapshot);

                if(clubId.equals(clubSchedule.getClubId())){
                    adapter.removeByKey(clubSchedule.getKey());
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final ClubSchedule clubSchedule = adapter.getItem(position);

                Log.d("TAG", "key: "+clubSchedule.getKey());
                Log.d("TAG", "id: " + Long.toString(id));
                Log.d("TAG", "position: " + Integer.toString(position));

                AlertDialog.Builder builder = new AlertDialog.Builder(ClubScheduleManageActivity.this);
                builder.setTitle("일정")
                        .setItems(new String[]{"수정", "삭제"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch(which){
                                    case 0:
                                        // start club schfedule edit activity
                                        Intent intent = new Intent(getApplicationContext(), ClubScheduleUpdateActivity.class);
                                        intent.putExtra("clubSchedule", clubSchedule);
                                        startActivity(intent);
                                        break;
                                    case 1:
                                        // delete club schedule activity
                                        ClubScheduleAPI.getInstance().removeClubSchedule(clubSchedule.getKey());
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });
                builder.create().show();
                return false;
            }
        });
    }
}
