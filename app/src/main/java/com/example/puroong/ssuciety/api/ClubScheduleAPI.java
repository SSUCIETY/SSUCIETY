package com.example.puroong.ssuciety.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.puroong.ssuciety.listeners.AfterQueryListener;
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

    public void getAllClubSchedules(final Context context, final AfterQueryListener listener) {
        DatabaseReference clubScheduleRef = FirebaseDatabase.getInstance().getReference().child(databaseName);

        clubScheduleRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.afterQuery(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Failed to get club schedules by clubId", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getClubSchedulesByClubId(String clubId, final Context context, final AfterQueryListener listener){
        DatabaseReference clubScheduleRef = FirebaseDatabase.getInstance().getReference().child(databaseName);

        clubScheduleRef.orderByChild("clubId").equalTo(clubId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.afterQuery(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Failed to get club schedules", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getClubScheduleByKey(String key, final Context context, final AfterQueryListener listener){
        DatabaseReference clubScheduleRef = FirebaseDatabase.getInstance().getReference().child(databaseName);

        clubScheduleRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.afterQuery(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Failed to get club schedule by key", Toast.LENGTH_SHORT).show();
            }
        });
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
                Club club = new Club(dataSnapshot);
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
                ClubSchedule temp = new ClubSchedule(dataSnapshot);
                String clubKey = temp.getClubId();

                rootRef.child(ClubAPI.databaseName).child(clubKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> childUpdates = new HashMap<>();

                        Map<String, Object> clubScheduleValues = finalClubSchedule.toMap();
                        String clubScheduleDatabasePath = "/"+databaseName+"/"+finalClubScheduleKey;
                        childUpdates.put(clubScheduleDatabasePath, clubScheduleValues);

                        String clubKey = dataSnapshot.getKey();
                        Club club = new Club(dataSnapshot);
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

    public void removeClubSchedule(final String key){
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final String finalClubScheduleKey = key;

        // get original clubschedule data
        // and update
        rootRef.child(databaseName).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ClubSchedule temp = new ClubSchedule(dataSnapshot);
                String clubKey = temp.getClubId();

                rootRef.child(ClubAPI.databaseName).child(clubKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> childUpdates = new HashMap<>();

                        String clubScheduleDatabasePath = "/"+databaseName+"/"+finalClubScheduleKey;
                        childUpdates.put(clubScheduleDatabasePath, null);

                        String clubKey = dataSnapshot.getKey();
                        Club club = new Club(dataSnapshot);
                        club.removeScheduleId(key);
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
