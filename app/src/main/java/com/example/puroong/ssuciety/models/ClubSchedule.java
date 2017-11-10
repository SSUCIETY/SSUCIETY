package com.example.puroong.ssuciety.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by puroong on 11/10/17.
 */

@IgnoreExtraProperties
public class ClubSchedule implements IFirebaseModel {
    private String title;
    private String startDate;
    private String endDate;
    private String clubId;

    private ClubSchedule(){}

    public ClubSchedule(String title, String startDate, String endDate, String clubId) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.clubId = clubId;
    }

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
}
