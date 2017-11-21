package com.example.puroong.ssuciety.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.puroong.ssuciety.models.Club;
import com.example.puroong.ssuciety.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by puroong on 11/11/17.
 */

public class ClubAPI {
    private static ClubAPI instance;
    private static DatabaseReference reference;
    public static final String databaseName = "clubs";
    private final String TAG = "ClubAPI";

    private ClubAPI(){}

    public static ClubAPI getInstance(){
        if(instance == null){
            reference = FirebaseDatabase.getInstance().getReference();
            instance = new ClubAPI();
        }

        return instance;
    }

    public void getClubByKey(String key, final Context context, final AfterQueryListener listener){
        DatabaseReference clubRef = FirebaseDatabase.getInstance().getReference().child(databaseName);

        clubRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.afterQuery(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Failed to get club by key", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void registerClub(Club club){
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final Club finalClub = club;

        // get user and update user
        rootRef.child(UserAPI.databaseName).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // add club
                // update user with club id
                Map<String, Object> childUpdates = new HashMap<>();

                String clubKey = rootRef.child(databaseName).push().getKey();
                Map<String, Object> clubValues = finalClub.toMap();
                String clubDatabasePath = "/"+databaseName+"/"+clubKey;
                childUpdates.put(clubDatabasePath, clubValues);

                String userKey = dataSnapshot.getKey();
                User user = dataSnapshot.getValue(User.class);
                user.setManagingClubId(clubKey);
                Map<String, Object> userValues = user.toMap();
                String userDatabasePath = "/"+UserAPI.databaseName+"/"+userKey;
                childUpdates.put(userDatabasePath, userValues);

                rootRef.updateChildren(childUpdates);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "registerClub canceled");
            }
        });
    }

    public void updateClub(String key, Club club){
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final Club finalClub = club;
        final String finalClubKey = key;

        rootRef.child(UserAPI.databaseName).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> childUpdates = new HashMap<>();

                String clubKey = finalClubKey;
                Map<String, Object> clubValues = finalClub.toMap();
                String clubDatabasePath = "/"+databaseName+"/"+clubKey;
                childUpdates.put(clubDatabasePath, clubValues);

                String userKey = dataSnapshot.getKey();
                User user = dataSnapshot.getValue(User.class);
                user.setManagingClubId(clubKey);
                Map<String, Object> userValues = user.toMap();
                String userDatabasePath = "/"+UserAPI.databaseName+"/"+userKey;
                childUpdates.put(userDatabasePath, userValues);

                rootRef.updateChildren(childUpdates);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "updateClub canceled");
            }
        });
    }

    public void removeClub(String key){
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final String finalClubKey = key;

        rootRef.child(UserAPI.databaseName).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> childUpdates = new HashMap<>();

                String clubKey = finalClubKey;
                String clubDatabasePath = "/"+databaseName+"/"+clubKey;
                childUpdates.put(clubDatabasePath, null);

                String userKey = dataSnapshot.getKey();
                User user = dataSnapshot.getValue(User.class);
                user.setManagingClubId(null);
                Map<String, Object> userValues = user.toMap();
                String userDatabasePath = "/"+UserAPI.databaseName+"/"+userKey;
                childUpdates.put(userDatabasePath, userValues);

                rootRef.updateChildren(childUpdates);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "updateClub canceled");
            }
        });
    }
}
