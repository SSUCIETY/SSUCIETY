package com.example.puroong.ssuciety.models;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by puroong on 11/10/17.
 */

@IgnoreExtraProperties
public class Club implements IFirebaseModel {
    private String key;
    private String name;
    private String wallpaperLink;
    private String introduction;
    private String description;
    private boolean hasClubroom;
    private String tagId;
    private String adminId;
    private Map<String, Boolean> activityIds;
    private Map<String, Boolean> scheduleIds;
    private Map<String, Boolean> starredUserIds;

    private Club(){}

    public Club(DataSnapshot dataSnapshot){
        Club club = dataSnapshot.getValue(Club.class);

        this.key = dataSnapshot.getKey();
        this.name = club.getName();
        this.wallpaperLink = club.getWallpaperLink();
        this.introduction = club.getIntroduction();
        this.description = club.getDescription();
        this.hasClubroom = club.isHasClubroom();
        this.tagId = club.getTagId();
        this.adminId = club.getAdminId();
        this.activityIds = club.getActivityIds();
        this.scheduleIds = club.getScheduleIds();
        this.starredUserIds = club.getStarredUserIds();

        if(this.activityIds == null){
            this.activityIds = new HashMap<>();
        }
        if(this.scheduleIds == null){
            this.scheduleIds = new HashMap<>();
        }
        if(this.starredUserIds == null){
            this.starredUserIds = new HashMap<>();
        }
    }

    public Club(String name, String wallpaperLink, String introduction, String description, boolean hasClubroom, String tagId, String adminId, Map<String, Boolean> activityIds, Map<String, Boolean> scheduleIds, Map<String, Boolean> starredUserIds) {
        this.name = name;
        this.wallpaperLink = wallpaperLink;
        this.introduction = introduction;
        this.description = description;
        this.hasClubroom = hasClubroom;
        this.tagId = tagId;
        this.adminId = adminId;
        this.activityIds = activityIds;
        this.scheduleIds = scheduleIds;
        this.starredUserIds = starredUserIds;

        if(this.activityIds == null){
            this.activityIds = new HashMap<>();
        }
        if(this.scheduleIds == null){
            this.scheduleIds = new HashMap<>();
        }
        if(this.starredUserIds == null){
            this.starredUserIds = new HashMap<>();
        }
    }

    public String getKey() { return key; }

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

    public Map<String, Boolean> getStarredUserIds() { return starredUserIds; }

    public void setKey(String key) { this.key = key; }

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

    public void appendStarredUserIds(String userId) { this.starredUserIds.put(userId, true); }

    public void removeActiviyId(String activityId) { this.activityIds.remove(activityId); }

    public void removeScheduleId(String scheduleId) { this.scheduleIds.remove(scheduleId); }

    public void removeStarredUserIds(String userId) { this.starredUserIds.remove(userId); }

    public boolean isStarred(String userId) {
        if(starredUserIds.containsKey(userId) && starredUserIds.get(userId)){
            return true;
        }
        else{
            return false;
        }
    }

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
        result.put("activityIds", activityIds);
        result.put("scheduleIds", scheduleIds);
        result.put("starredUserIds", starredUserIds);

        return result;
    }
}
