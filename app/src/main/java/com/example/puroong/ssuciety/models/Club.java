package com.example.puroong.ssuciety.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by puroong on 11/10/17.
 */

@IgnoreExtraProperties
public class Club implements IFirebaseModel {
    private String name;
    private String wallpaperLink;
    private String introduction;
    private String description;
    private boolean hasClubroom;
    private String tagId;
    private String adminId;
    private Map<String, Boolean> activityIds;
    private Map<String, Boolean> scheduleIds;

    private Club(){}

    public Club(String name, String wallpaperLink, String introduction, String description, boolean hasClubroom, String tagId, String adminId, Map<String, Boolean> activityIds, Map<String, Boolean> scheduleIds) {
        this.name = name;
        this.wallpaperLink = wallpaperLink;
        this.introduction = introduction;
        this.description = description;
        this.hasClubroom = hasClubroom;
        this.tagId = tagId;
        this.adminId = adminId;
        this.activityIds = activityIds;
        this.scheduleIds = scheduleIds;
    }

    public String getName() {
        return name;
    }

    public String getWallpaperLink() {
        return wallpaperLink;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getDescription() {
        return description;
    }

    public boolean isHasClubroom() {
        return hasClubroom;
    }

    public String getTagId() {
        return tagId;
    }

    public String getAdminId() {
        return adminId;
    }

    public Map<String, Boolean> getActivityIds() { return activityIds; }

    public Map<String, Boolean> getScheduleIds() { return scheduleIds; }

    public void setName(String name) {
        this.name = name;
    }

    public void setWallpaperLink(String wallpaperLink) {
        this.wallpaperLink = wallpaperLink;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHasClubroom(boolean hasClubRoom) {
        this.hasClubroom = hasClubRoom;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public void setActivityIds(Map<String, Boolean> activityIds) { this.activityIds = activityIds; }

    public void appendActiviyId(String activityId) { this.activityIds.put(activityId, true); }

    public void appendScheduleId(String scheduleId) { this.scheduleIds.put(scheduleId, true); }

    @Override
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("name", name);
        result.put("wallpaperLink", wallpaperLink);
        result.put("introduction", introduction);
        result.put("description", description);
        result.put("hasClubroom", hasClubroom);
        result.put("tagId", tagId);
        result.put("adminId", adminId);

        return result;
    }
}
