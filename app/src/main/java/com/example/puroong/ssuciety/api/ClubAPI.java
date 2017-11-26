package com.example.puroong.ssuciety.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.puroong.ssuciety.listeners.AfterQueryListener;
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

    public void getClubsByTag(String tagName, final Context context, final AfterQueryListener listener){
        DatabaseReference clubRef = FirebaseDatabase.getInstance().getReference().child(databaseName);

        clubRef.orderByChild("tagId").equalTo(tagName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.afterQuery(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Failed to get clubs by context", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void toggleStarClub(String clubKey, Club club, User user, boolean shouldStar) {
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        Map<String, Object> childUpdates = new HashMap<>();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(shouldStar) {
            club.appendStarredUserIds(uid);
            user.appendStarredClubId(clubKey);
        } else {
            club.removeStarredUserIds(uid);
            user.removeStarredClubId(clubKey);
        }
        Map<String, Object> clubValues = club.toMap();
        String clubDatabasePath = "/"+databaseName+"/"+clubKey;
        childUpdates.put(clubDatabasePath, clubValues);

        Map<String, Object> userValues = user.toMap();
        String userDatabasePath = "/"+UserAPI.databaseName+"/"+uid;
        childUpdates.put(userDatabasePath, userValues);

        rootRef.updateChildren(childUpdates);
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

    public void updateClub(String clubKey, Club club){
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final Club finalClub = club;

        Map<String, Object> childUpdates = new HashMap<>();

        Map<String, Object> clubValues = finalClub.toMap();
        String clubDatabasePath = "/"+databaseName+"/"+clubKey;
        childUpdates.put(clubDatabasePath, clubValues);

        rootRef.updateChildren(childUpdates);
    }

    public void removeClub(String clubKey){
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        Map<String, Object> childUpdates = new HashMap<>();

        String clubDatabasePath = "/"+databaseName+"/"+clubKey;
        childUpdates.put(clubDatabasePath, null);

        String userKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        User user = UserAPI.getInstance().getCurrentUser();
        user.setManagingClubId(null);
        Map<String, Object> userValues = user.toMap();
        String userDatabasePath = "/"+UserAPI.databaseName+"/"+userKey;
        childUpdates.put(userDatabasePath, userValues);

        rootRef.updateChildren(childUpdates);
    }
}
