package com.example.puroong.ssuciety.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.puroong.ssuciety.listeners.AfterQueryListener;
import com.example.puroong.ssuciety.models.Club;
import com.example.puroong.ssuciety.models.ClubActivity;
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

public class ClubActivityAPI {
    private static ClubActivityAPI instance;
    private static DatabaseReference reference;
    public static final String databaseName = "club-activities";
    private final String TAG = "ClubActivityAPI";

    private ClubActivityAPI(){}

    public static ClubActivityAPI getInstance(){
        if(instance == null){
            reference = FirebaseDatabase.getInstance().getReference();
            instance = new ClubActivityAPI();
        }

        return instance;
    }

    public void getClubActivityByKey(String key, final Context context, final AfterQueryListener listener){
        DatabaseReference clubRef = FirebaseDatabase.getInstance().getReference().child(databaseName);

        clubRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.afterQuery(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Failed to get club activity by key", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void registerClubActivity(final ClubActivity clubActivity){
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final ClubActivity finalClubActivity = clubActivity;

        rootRef.child(ClubAPI.databaseName).child(clubActivity.getClubId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> childUpdates = new HashMap<>();

                String clubActivityKey = rootRef.child(databaseName).push().getKey();
                Map<String, Object> clubActivityValues = finalClubActivity.toMap();
                String clubActivityDatabasePath = "/"+databaseName+"/"+clubActivityKey;
                childUpdates.put(clubActivityDatabasePath, clubActivityValues);

                String clubKey = dataSnapshot.getKey();
                Club club = dataSnapshot.getValue(Club.class);
                club.appendActiviyId(clubActivityKey);
                Map<String, Object> clubValues = club.toMap();
                String clubDatabasePath = "/"+ClubAPI.databaseName+"/"+clubKey;
                childUpdates.put(clubDatabasePath, clubValues);

                rootRef.updateChildren(childUpdates);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "registerClubActivity canceled");
            }
        });
    }

    public void updateClubActivity(String key, ClubActivity clubActivity){
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final ClubActivity finalClubActivity = clubActivity;
        final String finalClubActivityKey = key;

        // get original clubactivity data
        // and update
        rootRef.child(databaseName).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ClubActivity temp = dataSnapshot.getValue(ClubActivity.class);
                String clubKey = temp.getClubId();

                rootRef.child(ClubAPI.databaseName).child(clubKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> childUpdates = new HashMap<>();

                        Map<String, Object> clubActivityValues = finalClubActivity.toMap();
                        String clubActivityDatabasePath = "/"+databaseName+"/"+finalClubActivityKey;
                        childUpdates.put(clubActivityDatabasePath, clubActivityValues);

                        String clubKey = dataSnapshot.getKey();
                        Club club = dataSnapshot.getValue(Club.class);
                        club.appendActiviyId(finalClubActivityKey);
                        Map<String, Object> clubValues = club.toMap();
                        String clubDatabasePath = "/"+ClubAPI.databaseName+"/"+clubKey;
                        childUpdates.put(clubDatabasePath, clubValues);

                        rootRef.updateChildren(childUpdates);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "updateClubActivity canceled");
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void removeClubActivity(String key){
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final String finalClubActivityKey = key;

        // get original clubactivity data
        // and update
        rootRef.child(databaseName).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ClubActivity temp = dataSnapshot.getValue(ClubActivity.class);
                String clubKey = temp.getClubId();

                rootRef.child(ClubAPI.databaseName).child(clubKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> childUpdates = new HashMap<>();

                        String clubActivityDatabasePath = "/"+databaseName+"/"+finalClubActivityKey;
                        childUpdates.put(clubActivityDatabasePath, null);

                        String clubKey = dataSnapshot.getKey();
                        Club club = dataSnapshot.getValue(Club.class);
                        club.appendActiviyId(null);
                        Map<String, Object> clubValues = club.toMap();
                        String clubDatabasePath = "/"+ClubAPI.databaseName+"/"+clubKey;
                        childUpdates.put(clubDatabasePath, clubValues);

                        rootRef.updateChildren(childUpdates);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "removeClubActivity canceled");
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
