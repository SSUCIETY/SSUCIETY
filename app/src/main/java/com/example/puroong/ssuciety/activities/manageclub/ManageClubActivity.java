package com.example.puroong.ssuciety.activities.manageclub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.puroong.ssuciety.R;
import com.example.puroong.ssuciety.activities.ClubAdminTransferActivity;
import com.example.puroong.ssuciety.activities.ClubUpdateActivity;
import com.example.puroong.ssuciety.activities.clubschedulemanage.ClubScheduleManageActivity;

import java.util.ArrayList;

public class ManageClubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_club);

        ArrayList<String> arrayOfUsers = new ArrayList<>();

        arrayOfUsers.add("동아리 정보 수정");
        arrayOfUsers.add("일정 수정");
        arrayOfUsers.add("활동사진 관리");
        arrayOfUsers.add("관리자 권한 양도");

        MenuAdapter adapter = new MenuAdapter(this, arrayOfUsers);
        ListView listView = (ListView) findViewById(R.id.lvManage);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;

                switch(position){
                    case 0:
                        // edit club info
                        intent = new Intent(getApplicationContext(), ClubUpdateActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        // edit schedule
                        intent = new Intent(getApplicationContext(), ClubScheduleManageActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        break;
                    case 3:
                        // transfer admin
                        intent = new Intent(getApplicationContext(), ClubAdminTransferActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
