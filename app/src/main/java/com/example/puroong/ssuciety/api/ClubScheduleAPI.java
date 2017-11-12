package com.example.puroong.ssuciety.api;

import android.util.Log;

import com.example.puroong.ssuciety.models.Club;
import com.example.puroong.ssuciety.models.ClubSchedule;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by puroong on 11/12/17.
 */

public class ClubScheduleAPI {
    private static ClubScheduleAPI instance;
    private static DatabaseReference reference;
    public static final String databaseName = "club-schedules";
    private final String TAG = "ClubScheduleAPI";

    private ClubScheduleAPI(){}

    public static ClubScheduleAPI getInstance(){
        if(instance == null){
            reference = FirebaseDatabase.getInstance().getReference();
            instance = new ClubScheduleAPI();
        }

        return instance;
    }

    public void registerClubSchedule(final ClubSchedule clubSchedule){
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final ClubSchedule finalClubSchedule = clubSchedule;

        rootRef.child(ClubAPI.databaseName).child(clubSchedule.getClubId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> childUpdates = new HashMap<>();

                String clubScheduleKey = rootRef.child(databaseName).push().getKey();
                Map<String, Object> clubScheduleValues = finalClubSchedule.toMap();
                String clubScheduleDatabasePath = "/"+databaseName+"/"+clubScheduleKey;
                childUpdates.put(clubScheduleDatabasePath, clubScheduleValues);

                String clubKey = dataSnapshot.getKey();
                Club club = dataSnapshot.getValue(Club.class);
                club.appendScheduleId(clubScheduleKey);
                Map<String, Object> clubValues = club.toMap();
                String clubDatabasePath = "/"+ClubAPI.databaseName+"/"+clubKey;
                childUpdates.put(clubDatabasePath, clubValues);

                rootRef.updateChildren(childUpdates);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "registerClubSchedule canceled");
            }
        });
    }

    public void updateClubSchedule(String key, ClubSchedule clubSchedule){
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final ClubSchedule finalClubSchedule = clubSchedule;
        final String finalClubScheduleKey = key;

        // get original clubschedule data
        // and update
        rootRef.child(databaseName).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ClubSchedule temp = dataSnapshot.getValue(ClubSchedule.class);
                String clubKey = temp.getClubId();

                rootRef.child(ClubAPI.databaseName).child(clubKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> childUpdates = new HashMap<>();

                        Map<String, Object> clubScheduleValues = finalClubSchedule.toMap();
                        String clubScheduleDatabasePath = "/"+databaseName+"/"+finalClubScheduleKey;
                        childUpdates.put(clubScheduleDatabasePath, clubScheduleValues);

                        String clubKey = dataSnapshot.getKey();
                        Club club = dataSnapshot.getValue(Club.class);
                        club.appendActiviyId(finalClubScheduleKey);
                        Map<String, Object> clubValues = club.toMap();
                        String clubDatabasePath = "/"+ClubAPI.databaseName+"/"+clubKey;
                        childUpdates.put(clubDatabasePath, clubValues);

                        rootRef.updateChildren(childUpdates);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "updateClubScheulde canceled");
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void removeClubSchedule(String key){
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final String finalClubScheduleKey = key;

        // get original clubschedule data
        // and update
        rootRef.child(databaseName).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ClubSchedule temp = dataSnapshot.getValue(ClubSchedule.class);
                String clubKey = temp.getClubId();

                rootRef.child(ClubAPI.databaseName).child(clubKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> childUpdates = new HashMap<>();

                        String clubScheduleDatabasePath = "/"+databaseName+"/"+finalClubScheduleKey;
                        childUpdates.put(clubScheduleDatabasePath, null);

                        String clubKey = dataSnapshot.getKey();
                        Club club = dataSnapshot.getValue(Club.class);
                        club.appendScheduleId(null);
                        Map<String, Object> clubValues = club.toMap();
                        String clubDatabasePath = "/"+ClubAPI.databaseName+"/"+clubKey;
                        childUpdates.put(clubDatabasePath, clubValues);

                        rootRef.updateChildren(childUpdates);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "removeClubSchdule canceled");
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
