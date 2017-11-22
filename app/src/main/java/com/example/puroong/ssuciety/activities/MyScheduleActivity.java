package com.example.puroong.ssuciety.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.puroong.ssuciety.R;
import com.example.puroong.ssuciety.api.AfterQueryListener;
import com.example.puroong.ssuciety.api.ClubAPI;
import com.example.puroong.ssuciety.api.ClubScheduleAPI;
import com.example.puroong.ssuciety.api.UserAPI;
import com.example.puroong.ssuciety.models.Club;
import com.example.puroong.ssuciety.models.ClubSchedule;
import com.example.puroong.ssuciety.models.User;
import com.google.firebase.database.DataSnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_schedule);

        final Map<String, String> clubNames = new HashMap<>();

        // TODO: 얘네 알고리즘 개선

        ClubScheduleAPI.getInstance().getAllClubSchedules(getApplicationContext(), new AfterQueryListener() {
            @Override
            public void afterQuery(DataSnapshot dataSnapshot) {
                User user = UserAPI.getInstance().getCurrentUser();
                final ArrayList<ClubSchedule> clubSchedules = new ArrayList<ClubSchedule>();

                // filter club schedules
                clubSchedules.clear();
                for(DataSnapshot clubScheduleSnapshot : dataSnapshot.getChildren()){
                    final ClubSchedule clubSchedule = new ClubSchedule(clubScheduleSnapshot);

                    if(user.getStarredClubIds().containsKey(clubSchedule.getClubId())){
                        if(clubNames.get(clubSchedule.getClubId()) == null){
                            ClubAPI.getInstance().getClubByKey(clubSchedule.getClubId(), getApplicationContext(), new AfterQueryListener() {
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
                MaterialCalendarView view = (MaterialCalendarView) findViewById(R.id.calendarView);

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
                            AlertDialog.Builder builder = new AlertDialog.Builder(MyScheduleActivity.this);

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
    }
}
