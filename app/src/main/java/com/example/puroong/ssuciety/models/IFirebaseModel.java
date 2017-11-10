package com.example.puroong.ssuciety.models;

import com.google.firebase.database.Exclude;

import java.util.Map;

/**
 * Created by puroong on 11/10/17.
 */

public interface IFirebaseModel {
    @Exclude
    Map<String, Object> toMap();
}
