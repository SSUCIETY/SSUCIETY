package com.example.puroong.ssuciety.activities.clubinfo;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.puroong.ssuciety.R;
import com.example.puroong.ssuciety.activities.MyScheduleActivity;
import com.example.puroong.ssuciety.api.ClubAPI;
import com.example.puroong.ssuciety.api.ClubActivityAPI;
import com.example.puroong.ssuciety.api.ClubScheduleAPI;
import com.example.puroong.ssuciety.api.UserAPI;
import com.example.puroong.ssuciety.listeners.AfterImageLoadListener;
import com.example.puroong.ssuciety.listeners.AfterQueryListener;
import com.example.puroong.ssuciety.models.Club;
import com.example.puroong.ssuciety.models.ClubActivity;
import com.example.puroong.ssuciety.models.ClubSchedule;
import com.example.puroong.ssuciety.models.User;
import com.example.puroong.ssuciety.utils.ImageUtil;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Clubinfomation extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private static String clubKey;
    private static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clubinfomation);

        clubKey = getIntent().getStringExtra("clubKey");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = null;
            final View finalRootView;

            final User user = UserAPI.getInstance().getCurrentUser();
            String clubId = null;

            switch ( getArguments().getInt(ARG_SECTION_NUMBER)){
                case 1:
                    rootView = inflater.inflate(R.layout.activity_clubinf, container, false);
                    finalRootView = rootView;

                    clubId = clubKey;

                    ClubAPI.getInstance().getClubByKey(clubId, rootView.getContext(), new AfterQueryListener() {
                        @Override
                        public void afterQuery(DataSnapshot dataSnapshot) {
                            final Club club = new Club(dataSnapshot);

                            final ImageView clubWallpaper = (ImageView) finalRootView.findViewById(R.id.imageView2);
                            final TextView clubName = (TextView) finalRootView.findViewById(R.id.club_name);
                            final TextView clubDescription = (TextView) finalRootView.findViewById(R.id.club_explain);
                            final TextView clubHasRoom = (TextView) finalRootView.findViewById(R.id.club_room_exist);
                            final TextView clubTagType = (TextView) finalRootView.findViewById(R.id.club_tag);
                            final TextView phoneNumber = (TextView) finalRootView.findViewById(R.id.club_phonenumber);

                            // load image
                            ImageUtil.getInstance().loadImage(finalRootView.getContext(), handler, club.getWallpaperLink(), new AfterImageLoadListener() {
                                @Override
                                public void setImage(Bitmap bitmap) {
                                    ImageUtil.getInstance().setImage(clubWallpaper, bitmap, true);
                                }
                            });

                            UserAPI.getInstance().getUserByUid(club.getAdminId(), finalRootView.getContext(), new AfterQueryListener() {
                                @Override
                                public void afterQuery(DataSnapshot dataSnapshot) {
                                    User admin = new User(dataSnapshot);

                                    String roomString = null;
                                    if(club.isHasClubroom()){
                                        roomString = "예";
                                    } else {
                                        roomString = "아니오";
                                    }

                                    // set the rest
                                    clubName.setText(club.getName());
                                    clubDescription.setText(club.getDescription());
                                    clubHasRoom.setText(roomString);
                                    clubTagType.setText(club.getTagId());
                                    phoneNumber.setText(admin.getPhoneNumber());
                                }
                            });
                        }
                    });
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.activity_club_act_pic, container, false);
                    finalRootView = rootView;

                    clubId = clubKey;

                    ListView listView = (ListView)rootView.findViewById(R.id.listview);
                    final ClubActPicAdapter cap= new ClubActPicAdapter(rootView.getContext(), new ArrayList<ClubActivity>());
                    listView.setAdapter(cap);

                    DatabaseReference clubActivityRef = FirebaseDatabase.getInstance().getReference().child(ClubActivityAPI.databaseName);

                    final String finalClubId = clubId;
                    clubActivityRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            ClubActivity clubActivity = new ClubActivity(dataSnapshot);
                            if(finalClubId.equals(clubActivity.getClubId())){
                                cap.add(clubActivity);
                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            ClubActivity clubActivity = new ClubActivity(dataSnapshot);

                            if(finalClubId.equals(clubActivity.getClubId())){
                                cap.updateByKey(clubActivity.getKey(), clubActivity);
                            }
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            ClubActivity clubActivity = new ClubActivity(dataSnapshot);

                            if(finalClubId.equals(clubActivity.getClubId())){
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

                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.activity_club_schedule, container, false);
                    finalRootView = rootView;

                    final Map<String, String> clubNames = new HashMap<>();

                    ClubScheduleAPI.getInstance().getAllClubSchedules(rootView.getContext(), new AfterQueryListener() {
                        @Override
                        public void afterQuery(DataSnapshot dataSnapshot) {
                            final ArrayList<ClubSchedule> clubSchedules = new ArrayList<ClubSchedule>();

                            // filter club schedules
                            clubSchedules.clear();
                            for (DataSnapshot clubScheduleSnapshot : dataSnapshot.getChildren()) {
                                final ClubSchedule clubSchedule = new ClubSchedule(clubScheduleSnapshot);

                                if (clubSchedule.getClubId().equals(clubKey)) {
                                    if (clubNames.get(clubSchedule.getClubId()) == null) {
                                        ClubAPI.getInstance().getClubByKey(clubSchedule.getClubId(), finalRootView.getContext(), new AfterQueryListener() {
                                            @Override
                                            public void afterQuery(DataSnapshot dataSnapshot) {
                                                Club club = new Club(dataSnapshot);

                                                clubNames.put(clubSchedule.getClubId(), club.getName());
                                            }
                                        });
                                    }

                                    clubSchedules.add(clubSchedule);
                                }
                            }
                            // set calendar
                            MaterialCalendarView view = (MaterialCalendarView) finalRootView.findViewById(R.id.calendarView);

                            view.addDecorator(new DayViewDecorator() {
                                @Override
                                public boolean shouldDecorate(CalendarDay day) {
                                    for(ClubSchedule clubSchedule : clubSchedules){
                                        if(clubSchedule.isActiveSchedule(day)){
                                            return true;
                                        }
                                    }

                                    return false;
                                }

                                @Override
                                public void decorate(DayViewFacade view) {
                                    view.addSpan(new DotSpan(5, Color.RED));
                                }
                            });

                            view.setOnDateChangedListener(new OnDateSelectedListener() {
                                @Override
                                public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
                                    String title = date.getYear() + "." + date.getMonth() + "." + date.getDay();
                                    ArrayList<String> events = new ArrayList<String>();

                                    for(ClubSchedule clubSchedule : clubSchedules){
                                        if(clubSchedule.isActiveSchedule(date)){
                                            String clubScheduleString = clubNames.get(clubSchedule.getClubId()) + ": " + clubSchedule.getTitle() + "\n시작일: " + clubSchedule.getStartDate() + "\n종료일: " + clubSchedule.getEndDate();
                                            events.add(clubScheduleString);
                                        }
                                    }

                                    if(events.size() > 0){
                                        AlertDialog.Builder builder = new AlertDialog.Builder(finalRootView.getContext());

                                        builder.setTitle(title)
                                                .setItems(events.toArray(new String[events.size()]), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                });

                                        builder.create().show();
                                    }
                                }
                            });
                        }
                    });
                    break;
            }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "동아리 정보";
                case 1:
                    return "활동 사진 보기";
                case 2:
                    return "일정 보기";
            }
            return null;
        }
    }
}
