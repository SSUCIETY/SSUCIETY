package com.example.puroong.ssuciety.activities.clublist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.puroong.ssuciety.R;
import com.example.puroong.ssuciety.models.Club;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * Created by puroong on 11/18/17.
 */

public class ClubListAdapter extends ArrayAdapter<Club> {
    private Club club;

    public ClubListAdapter(Context context, ArrayList<Club> clubs) {
        super(context, 0, clubs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        club = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.club_list_item, parent, false);
        }
        // Lookup view for data population
        TextView tvClubName = (TextView) convertView.findViewById(R.id.tvClubName);
        TextView tvClubIntro = (TextView) convertView.findViewById(R.id.tvClubIntro);
        final ImageView toggleStar = (ImageView) convertView.findViewById(R.id.ivToggleStar);

        // Populate the data into the template view using the data object
        tvClubName.setText(club.getName());
        tvClubIntro.setText(club.getIntroduction());

        toggleStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(club.isStarred("test")){
                    toggleStar.setImageResource(android.R.drawable.star_big_off);
                    // TODO: Remove user uid from club starredUserIds
                }
                else{
                    toggleStar.setImageResource(android.R.drawable.star_big_on);
                    // TODO: Append user uid to club starredUserIds
                }
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }
}
