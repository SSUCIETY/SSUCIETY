package com.example.puroong.ssuciety.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by puroong on 11/10/17.
 */

@IgnoreExtraProperties
public class ClubSchedule implements IFirebaseModel, Parcelable {
    private String key;
    private String title;
    private String startDate;
    private String endDate;
    private String clubId;

    private ClubSchedule(){}

    public ClubSchedule(DataSnapshot dataSnapshot){
        ClubSchedule clubSchedule = dataSnapshot.getValue(ClubSchedule.class);

        this.key = dataSnapshot.getKey();
        this.title = clubSchedule.title;
        this.startDate = clubSchedule.startDate;
        this.endDate = clubSchedule.endDate;
        this.clubId = clubSchedule.clubId;
    }

    public ClubSchedule(String title, String startDate, String endDate, String clubId) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.clubId = clubId;
    }

    protected ClubSchedule(Parcel in) {
        key = in.readString();
        title = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        clubId = in.readString();
    }

    public static final Creator<ClubSchedule> CREATOR = new Creator<ClubSchedule>() {
        @Override
        public ClubSchedule createFromParcel(Parcel in) {
            return new ClubSchedule(in);
        }

        @Override
        public ClubSchedule[] newArray(int size) {
            return new ClubSchedule[size];
        }
    };

    public String getKey() { return key; }

    public String getTitle() {
        return title;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getClubId() {
        return clubId;
    }

    public void setKey(String key) { this.key = key; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public boolean isActiveSchedule(CalendarDay day) {
        String[] parseStartDate = this.startDate.split("-");
        String[] parseEndDate = this.endDate.split("-");

        int startYear = Integer.parseInt(parseStartDate[0]);
        int startMonth = Integer.parseInt(parseStartDate[1])-1;
        int startDate = Integer.parseInt(parseStartDate[2]);

        int endYear = Integer.parseInt(parseEndDate[0]);
        int endMonth = Integer.parseInt(parseEndDate[1])-1;
        int endDate = Integer.parseInt(parseEndDate[2]);

        CalendarDay startDateToCalendarDay = CalendarDay.from(startYear, startMonth, startDate);
        CalendarDay endDateToCalendarDay = CalendarDay.from(endYear, endMonth, endDate);

        boolean isInRange = day.isInRange(startDateToCalendarDay, endDateToCalendarDay);

        if(isInRange){
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("title", title);
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("clubId", clubId);

        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(title);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(clubId);
    }
}
