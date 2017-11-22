package com.example.puroong.ssuciety.models;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by puroong on 11/10/17.
 */

@IgnoreExtraProperties
public class User implements IFirebaseModel {
    private String uid;
    private String name;
    private String email;
    private String phoneNumber;
    private String profileLink;
    private String major;
    private int admissionYear;
    private int gender;
    private String managingClubId;
    private Map<String, Boolean> starredClubIds;

    private User(){}

    public User(DataSnapshot dataSnapshot){
        User user = dataSnapshot.getValue(User.class);

        this.uid = dataSnapshot.getKey();
        this.name = user.name;
        this.email = user.email;
        this.phoneNumber = user.phoneNumber;
        this.profileLink = user.profileLink;
        this.major = user.major;
        this.admissionYear = user.admissionYear;
        this.gender = user.gender;
        this.managingClubId = user.managingClubId;
        this.starredClubIds = user.starredClubIds;

        if(this.starredClubIds == null){
            this.starredClubIds = new HashMap<>();
        }
    }

    public User(String name, String email, String phoneNumber, String profileLink, String major, int admissionYear, int gender, String managingClubId, Map<String, Boolean> starredClubIds) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profileLink = profileLink;
        this.major = major;
        this.admissionYear = admissionYear;
        this.gender = gender;
        this.managingClubId = managingClubId;
        this.starredClubIds = starredClubIds;

        if(this.starredClubIds == null){
            this.starredClubIds = new HashMap<>();
        }
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getProfileLink() {
        return profileLink;
    }

    public String getMajor() {
        return major;
    }

    public int getAdmissionYear() {
        return admissionYear;
    }

    public int getGender() {
        return gender;
    }

    public String getManagingClubId() {
        return managingClubId;
    }

    public Map<String, Boolean> getStarredClubIds() {
        return starredClubIds;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setProfileLink(String profileLink) {
        this.profileLink = profileLink;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setAdmissionYear(int admissionYear) {
        this.admissionYear = admissionYear;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setManagingClubId(String managingClubId) {
        this.managingClubId = managingClubId;
    }

    public void setStarredClubIds(Map<String, Boolean> starredClubIds) {
        this.starredClubIds = starredClubIds;
    }

    public void appendStarredClubId(String clubId){
        this.starredClubIds.put(clubId, true);
    }

    public void removeStarredClubId(String clubId){
        this.starredClubIds.remove(clubId);
    }

    @Override
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("name", name);
        result.put("email", email);
        result.put("phoneNumber", phoneNumber);
        result.put("profileLink", profileLink);
        result.put("major", major);
        result.put("admissionYear", admissionYear);
        result.put("gender", gender);
        result.put("managingClubId", managingClubId);
        result.put("starredClubIds", starredClubIds);

        return result;
    }
}
