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
public class Tag implements IFirebaseModel {
    private String name;
    private List<String> clubIds;

    private Tag(){}

    public Tag(String name, List<String> clubIds) {
        this.name = name;
        this.clubIds = clubIds;
    }

    public String getName() {
        return name;
    }

    public List<String> getClubIds() {
        return clubIds;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClubIds(List<String> clubIds) {
        this.clubIds = clubIds;
    }

    @Override
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("name", name);
        result.put("clubIds", clubIds);

        return result;
    }
}
