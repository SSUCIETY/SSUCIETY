package com.example.puroong.ssuciety.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.puroong.ssuciety.models.User;
import com.google.firebase.auth.FirebaseAuth;
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
    User user;
    private UserAPI(){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child(databaseName);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = new User(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static UserAPI getInstance(){
        if(instance == null){
            instance = new UserAPI();
        }

        return instance;
    }

    public User getCurrentUser() {
        return user;
    }

    public void getUserByUid(String uid, final Context context, final AfterQueryListener listener){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child(databaseName);

        userRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.afterQuery(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Failed to get user by uid", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getUserByContact(String contact, final Context context, final AfterQueryListener listener){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child(databaseName);

        userRef.orderByChild("phoneNumber").equalTo(contact).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.afterQuery(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Failed to get user by contact", Toast.LENGTH_SHORT).show();
            }
        });
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
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child(databaseName);
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
