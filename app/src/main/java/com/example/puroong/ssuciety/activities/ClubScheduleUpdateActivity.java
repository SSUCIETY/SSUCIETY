package com.example.puroong.ssuciety.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.puroong.ssuciety.R;
import com.example.puroong.ssuciety.activities.clubschedulemanage.ClubScheduleManageActivity;
import com.example.puroong.ssuciety.api.AfterQueryListener;
import com.example.puroong.ssuciety.api.ClubAPI;
import com.example.puroong.ssuciety.api.ClubScheduleAPI;
import com.example.puroong.ssuciety.api.UserAPI;
import com.example.puroong.ssuciety.models.Club;
import com.example.puroong.ssuciety.models.ClubSchedule;
import com.example.puroong.ssuciety.models.User;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Calendar;

public class ClubScheduleUpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_schedule_form);

        final ClubSchedule clubSchedule = getIntent().getParcelableExtra("clubSchedule");

        final EditText etTitle = (EditText) findViewById(R.id.etTitle);

        final Spinner spStartDateYear = (Spinner) findViewById(R.id.spStartDateYear);
        final Spinner spStartDateMonth = (Spinner) findViewById(R.id.spStartDateMonth);
        final Spinner spStartDateDay = (Spinner) findViewById(R.id.spStartDateDay);

        final Spinner spEndDateYear = (Spinner) findViewById(R.id.spEndDateYear);
        final Spinner spEndDateMonth = (Spinner) findViewById(R.id.spEndDateMonth);
        final Spinner spEndDateDay = (Spinner) findViewById(R.id.spEndDateDay);


        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        int thisMonth = Calendar.getInstance().get(Calendar.MONTH);
        int thisDay = Calendar.getInstance().get(Calendar.DATE);

        // set start year, end year
        ArrayList<String> years = new ArrayList<String>();
        for (int i = thisYear; i >= 1970; i--) {
            years.add(Integer.toString(i));
        }

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);

        spStartDateYear.setAdapter(yearAdapter);
        spEndDateYear.setAdapter(yearAdapter);
        // set start month, end month
        ArrayList<String> months = new ArrayList<String>();
        for (int i = 1; i <= 12; i++) {
            months.add(Integer.toString(i));
        }

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, months);

        spStartDateMonth.setAdapter(monthAdapter);
        spEndDateMonth.setAdapter(monthAdapter);
        // set start day, end day
        ArrayList<String> days = new ArrayList<String>();
        for (int i = 1; i <= 31; i++) {
            days.add(Integer.toString(i));
        }

        ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, days);

        spStartDateDay.setAdapter(dayAdapter);
        spEndDateDay.setAdapter(dayAdapter);

        // set original data
        int position;

        etTitle.setText(clubSchedule.getTitle());
        String[] startDate = clubSchedule.getStartDate().split("-");
        String[] endDate = clubSchedule.getEndDate().split("-");

        position = thisYear - Integer.parseInt(startDate[0]);
        spStartDateYear.setSelection(position);

        position = Integer.parseInt(startDate[1]) - 1;
        spStartDateMonth.setSelection(position);

        position = Integer.parseInt(startDate[2]) - 1;
        spStartDateDay.setSelection(position);


        position = thisYear - Integer.parseInt(endDate[0]);
        spEndDateYear.setSelection(position);

        position = Integer.parseInt(endDate[1]) - 1;
        spEndDateMonth.setSelection(position);

        position = Integer.parseInt(startDate[2]) - 1;
        spEndDateDay.setSelection(position);
        // add submit click listener
        // set title, set startday, set endday
        Button submit = (Button) findViewById(R.id.btnSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final User user = UserAPI.getInstance().getCurrentUser();

                ClubAPI.getInstance().getClubByKey(user.getManagingClubId(), getApplicationContext(), new AfterQueryListener() {
                    @Override
                    public void afterQuery(DataSnapshot dataSnapshot) {
                        Club club = new Club(dataSnapshot);

                        String title = etTitle.getText().toString();
                        String startDate;
                        String endDate;

                        String startDateYear = spStartDateYear.getSelectedItem().toString();
                        String startDateMonth = spStartDateMonth.getSelectedItem().toString();
                        String startDateDay = spStartDateDay.getSelectedItem().toString();

                        String endDateYear = spEndDateYear.getSelectedItem().toString();
                        String endDateMonth = spEndDateMonth.getSelectedItem().toString();
                        String endDateDay = spEndDateDay.getSelectedItem().toString();

                        startDate = (startDateYear + "-" + startDateMonth + "-" + startDateDay);
                        endDate = (endDateYear + "-" + endDateMonth + "-" + endDateDay);


                        clubSchedule.setTitle(title);
                        clubSchedule.setStartDate(startDate);
                        clubSchedule.setEndDate(endDate);

                        ClubScheduleAPI.getInstance().updateClubSchedule(clubSchedule.getKey(), clubSchedule);

                        Intent intent = new Intent(getApplicationContext(), ClubScheduleManageActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}
