package com.example.puroong.ssuciety.activities.managepic;

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
import android.widget.ImageView;
import android.widget.ListView;

import com.example.puroong.ssuciety.R;
import com.example.puroong.ssuciety.api.ClubActivityAPI;
import com.example.puroong.ssuciety.api.ClubScheduleAPI;
import com.example.puroong.ssuciety.api.UserAPI;
import com.example.puroong.ssuciety.models.ClubActivity;
import com.example.puroong.ssuciety.models.ClubSchedule;
import com.example.puroong.ssuciety.models.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ManagePic extends AppCompatActivity {
    ListView listView;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_pic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),SelectPic.class);
                startActivity(intent);
            }
        });

        listView= (ListView)findViewById(R.id.listview);
        ArrayList<ClubActivity> List= new ArrayList<ClubActivity>();
        final ManPicAdapter cap= new ManPicAdapter(getApplicationContext(), new ArrayList<ClubActivity>());
        listView.setAdapter(cap);

        User user = UserAPI.getInstance().getCurrentUser();
        final String clubId = user.getManagingClubId();

        DatabaseReference clubActivityRef = FirebaseDatabase.getInstance().getReference().child(ClubActivityAPI.databaseName);

        clubActivityRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ClubActivity clubActivity = new ClubActivity(dataSnapshot);
                Log.d("TAG", "clubactivity added: "+clubActivity.toMap().toString());
                if(clubId.equals(clubActivity.getClubId())){
                    cap.add(clubActivity);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                ClubActivity clubActivity = new ClubActivity(dataSnapshot);
                Log.d("TAG", "clubactivity changed: "+clubActivity.toMap().toString());

                if(clubId.equals(clubActivity.getClubId())){
                    cap.updateByKey(clubActivity.getKey(), clubActivity);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                ClubActivity clubActivity = new ClubActivity(dataSnapshot);
                Log.d("TAG", "clubactivity removed" +
                        ": "+clubActivity.toMap().toString());

                if(clubId.equals(clubActivity.getClubId())){
                    cap.removeByKey(clubActivity.getKey());
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
                final ClubActivity clubActivity = cap.getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(ManagePic.this);
                builder.setTitle("활동사진")
                        .setItems(new String[]{"수정", "삭제"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch(which){
                                    case 0:
                                        // start club schfedule edit activity
                                        Intent intent = new Intent(getApplicationContext(), UpdatePic.class);
                                        intent.putExtra("clubActivity", clubActivity);
                                        startActivity(intent);
                                        break;
                                    case 1:
                                        // delete club schedule activity
                                        ClubActivityAPI.getInstance().removeClubActivity(clubActivity.getKey());
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
