package com.example.puroong.ssuciety.activities.managepic;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.puroong.ssuciety.R;
import com.example.puroong.ssuciety.models.ClubActivity;

import java.util.ArrayList;

public class ManagePic extends AppCompatActivity {
    ListView listView;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_pic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),SelectPic.class);
                startActivity(intent);
            }
        });
        listView= (ListView)findViewById(R.id.listview);
        ArrayList<ClubActivity> List= new ArrayList<ClubActivity>();
        ManPicAdapter cap= new ManPicAdapter(getBaseContext(),List);
        listView.setAdapter(cap);
        ClubActivity a= new ClubActivity("pic1","bbc","abc");
        ClubActivity b= new ClubActivity("pic2","cbc","gbc");
        ClubActivity c= new ClubActivity("pic3","dbc","fbc");
        List.add(a);
        List.add(b);
        List.add(c);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
    }

}
