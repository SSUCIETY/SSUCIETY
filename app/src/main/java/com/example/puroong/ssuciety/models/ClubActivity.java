package com.example.puroong.ssuciety.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by puroong on 11/10/17.
 */

@IgnoreExtraProperties
public class ClubActivity implements IFirebaseModel, Parcelable {
    private String key;
    private String name;
    private String pictureLink;
    private String clubId;

    private ClubActivity(){}

    public ClubActivity(DataSnapshot dataSnapshot) {
        ClubActivity clubActivity = dataSnapshot.getValue(ClubActivity.class);

        this.key = dataSnapshot.getKey();
        this.name = clubActivity.name;
        this.pictureLink = clubActivity.pictureLink;
        this.clubId = clubActivity.clubId;
    }

    public ClubActivity(String name, String pictureLink, String clubId) {
        this.name = name;
        this.pictureLink = pictureLink;
        this.clubId = clubId;
    }

    protected ClubActivity(Parcel in) {
        key = in.readString();
        name = in.readString();
        pictureLink = in.readString();
        clubId = in.readString();
    }

    public static final Creator<ClubActivity> CREATOR = new Creator<ClubActivity>() {
        @Override
        public ClubActivity createFromParcel(Parcel in) {
            return new ClubActivity(in);
        }

        @Override
        public ClubActivity[] newArray(int size) {
            return new ClubActivity[size];
        }
    };

    public String getKey() { return key; }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(pictureLink);
        dest.writeString(clubId);
    }
}
