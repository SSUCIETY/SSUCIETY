package com.example.puroong.ssuciety.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by puroong on 11/10/17.
 */

@IgnoreExtraProperties
public class ClubActivity implements IFirebaseModel {
    private String name;
    private String pictureLink;
    private String clubId;

    private ClubActivity(){}

    public ClubActivity(String name, String pictureLink, String clubId) {
        this.name = name;
        this.pictureLink = pictureLink;
        this.clubId = clubId;
    }

    public String getName() {
        return name;
    }

    public String getPictureLink() {
        return pictureLink;
    }

    public String getClubId() {
        return clubId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPictureLink(String pictureLink) {
        this.pictureLink = pictureLink;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    @Override
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("name", name);
        result.put("pictureLink", pictureLink);
        result.put("clubId", clubId);

        return result;
    }
}
