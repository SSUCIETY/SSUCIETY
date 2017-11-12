package com.example.puroong.ssuciety.api;

import android.util.Log;

import com.example.puroong.ssuciety.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by puroong on 11/11/17.
 */

public class UserAPI {
    private static UserAPI instance;
    public static final String databaseName = "users";
    private final String TAG = "UserAPI";

    private UserAPI(){}

    public static UserAPI getInstance(){
        if(instance == null){
            instance = new UserAPI();
        }

        return instance;
    }

    public void registerUser(String uid, User user){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child(databaseName);
        String key = uid;
        Map<String, Object> userValues = user.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        String databasePath = "/"+key;
        childUpdates.put(databasePath, userValues);

        try{
            userRef.updateChildren(childUpdates);
        }
        catch(DatabaseException exp){
            Log.w(TAG, exp.toString());
        }
    }

    public void updateUser(String key, User user){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> userValues = user.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        String databasePath = "/"+key;
        childUpdates.put(databasePath, userValues);

        try{
            userRef.updateChildren(childUpdates);
        }
        catch(DatabaseException exp){
            Log.w(TAG, exp.toString());
        }
    }

    public void removeUser(String key){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        String databasePath = "/"+key;
        childUpdates.put(databasePath, null);

        try{
            userRef.updateChildren(childUpdates);
        }
        catch(DatabaseException exp){
            Log.w(TAG, exp.toString());
        }
    }
}
