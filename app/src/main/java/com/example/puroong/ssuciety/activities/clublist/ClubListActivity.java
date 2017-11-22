package com.example.puroong.ssuciety.activities.clublist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.puroong.ssuciety.R;
import com.example.puroong.ssuciety.activities.ClubSubmitActivity;
import com.example.puroong.ssuciety.activities.FacebookLoginActivity;
import com.example.puroong.ssuciety.activities.MyScheduleActivity;
import com.example.puroong.ssuciety.activities.manageclub.ManageClubActivity;
import com.example.puroong.ssuciety.api.AfterQueryListener;
import com.example.puroong.ssuciety.api.ClubAPI;
import com.example.puroong.ssuciety.api.UserAPI;
import com.example.puroong.ssuciety.models.Club;
import com.example.puroong.ssuciety.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClubListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView tagText;
    private Spinner tagType;
    private ListView clubList;
    private boolean userIsInteracting;

    @Override
    protected void onStart() {
        super.onStart();

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(), FacebookLoginActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_list);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        UserAPI.getInstance().getUserByUid(uid, getApplicationContext(), new AfterQueryListener() {
            @Override
            public void afterQuery(DataSnapshot dataSnapshot) {
                updateUI();
            }
        });
    }
    private void updateUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        // hide manage_club/register_club menu item
        final Menu navMenu = navigationView.getMenu();

        User user = UserAPI.getInstance().getCurrentUser();

        MenuItem registerClub = navMenu.findItem(R.id.register_club);
        MenuItem manageClub = navMenu.findItem(R.id.manage_club);

        if(user.getManagingClubId() == null) {
            manageClub.setVisible(false);
            registerClub.setVisible(true);
        } else {
            manageClub.setVisible(true);
            registerClub.setVisible(false);
        }

        navigationView.setNavigationItemSelectedListener(this);

        tagText = (TextView) findViewById(R.id.tvTagText);
        tagType = (Spinner) findViewById(R.id.spTagType);
        clubList = (ListView) findViewById(R.id.lvClubList);

        // init tvTagText text
        String text = tagType.getSelectedItem().toString();
        tagText.setText(text);

        final ClubListAdapter adapter = new ClubListAdapter(ClubListActivity.this, new ArrayList<Club>());
        clubList.setAdapter(adapter);

        // set listview content
        rootRef.child(ClubAPI.databaseName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Club club = new Club(dataSnapshot);

                if(club.getTagId().equals(tagText.getText().toString())){
                    adapter.add(club);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Club club = new Club(dataSnapshot);

                if(club.getTagId().equals(tagText.getText().toString())){
                    adapter.updateByKey(club.getKey(), club);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Club club = new Club(dataSnapshot);

                if(club.getTagId().equals(tagText.getText().toString())){
                    adapter.removeByKey(club.getKey());
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // set spinner click listener
        tagType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(userIsInteracting){
                    String text = tagType.getSelectedItem().toString();
                    tagText.setText(text);

                    // set listview content
                    ClubAPI.getInstance().getClubsByTag(tagText.getText().toString(), getApplicationContext(), new AfterQueryListener() {
                        @Override
                        public void afterQuery(DataSnapshot dataSnapshot) {
                            adapter.clear();

                            for(DataSnapshot clubSnapshot : dataSnapshot.getChildren()){
                                adapter.add(new Club(clubSnapshot));
                            }
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    @Override
    public void onUserInteraction(){
        super.onUserInteraction();
        userIsInteracting = true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            // TODO: move to profile actiity
        } else if (id == R.id.register_club) {
            startActivity(new Intent(getApplicationContext(), ClubSubmitActivity.class));
        } else if (id == R.id.manage_club) {
            startActivity(new Intent(getApplicationContext(), ManageClubActivity.class));
        } else if (id == R.id.my_schedules) {
            startActivity(new Intent(getApplicationContext(), MyScheduleActivity.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
