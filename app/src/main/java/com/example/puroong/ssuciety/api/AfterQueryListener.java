package com.example.puroong.ssuciety.api;

import com.example.puroong.ssuciety.models.IFirebaseModel;
import com.google.firebase.database.DataSnapshot;

/**
 * Created by puroong on 11/21/17.
 */

public interface AfterQueryListener {
    void afterQuery(DataSnapshot dataSnapshot);
}
