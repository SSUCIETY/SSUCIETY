package com.example.puroong.ssuciety.activities.clublist;

import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.example.puroong.ssuciety.models.Club;

import java.util.ArrayList;

public class ClubListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView tagText;
    private Spinner tagType;
    private ListView clubList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        // hide manage_club/register_club menu item
        Menu navMenu = navigationView.getMenu();
        // TODO: if user owns club, hide register_club
        // TODO: if user does not own club, hide manage_club

        navigationView.setNavigationItemSelectedListener(this);

        tagText = (TextView) findViewById(R.id.tvTagText);
        tagType = (Spinner) findViewById(R.id.spTagType);
        clubList = (ListView) findViewById(R.id.lvClubList);

        // init tvTagText text
        String text = tagType.getSelectedItem().toString();
        tagText.setText(text);

        // set listview content
        ArrayList<Club> clubs = new ArrayList<>();

        // TODO: fetch and set club items

        ClubListAdapter adapter = new ClubListAdapter(ClubListActivity.this, clubs);
        clubList.setAdapter(adapter);

        // set spinner click listener
        tagType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String text = tagType.getSelectedItem().toString();
                tagText.setText(text);

                // set listview content
                ArrayList<Club> clubs = new ArrayList<>();

                // TODO: fetch club and set clearitems

                ClubListAdapter adapter = new ClubListAdapter(ClubListActivity.this, clubs);
                clubList.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
            // TODO: move to reigister club activity
        } else if (id == R.id.manage_club) {
            // TODO: move to manage club activity
        } else if (id == R.id.my_schedules) {
            // TODO: move to schedule activity
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
