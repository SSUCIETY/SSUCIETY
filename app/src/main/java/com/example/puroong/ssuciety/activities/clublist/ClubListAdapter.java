package com.example.puroong.ssuciety.activities.clublist;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.puroong.ssuciety.R;
import com.example.puroong.ssuciety.api.ClubAPI;
import com.example.puroong.ssuciety.api.UserAPI;
import com.example.puroong.ssuciety.listeners.AfterImageLoadListener;
import com.example.puroong.ssuciety.models.Club;
import com.example.puroong.ssuciety.models.User;
import com.example.puroong.ssuciety.utils.ImageUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by puroong on 11/18/17.
 */

public class ClubListAdapter extends ArrayAdapter<Club> {
    private List<Club> clubs = new ArrayList<>();
    private Handler handler = new Handler();

    public ClubListAdapter(Context context, ArrayList<Club> clubs) {
        super(context, 0, clubs);
        this.clubs = clubs;
    }

    public void add(Club item) {
        clubs.add(item);
        notifyDataSetChanged();
    }

    public void updateByKey(String key, Club updated) {
        int index = 0;

        for(Club club : clubs){
            if(key.equals(club.getKey())){
                clubs.remove(club);
                clubs.add(index, updated);
                notifyDataSetChanged();
                break;
            }
            index++;
        }
    }

    public void removeByKey(String key) {
        for(Club club : clubs){
            if(key.equals(club.getKey())){
                clubs.remove(club);
                notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final User user = UserAPI.getInstance().getCurrentUser();

        // Get the data item for this position
        final Club club = getItem(position);
        // inflate the view; ah molla da async ddaemooniya
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.club_list_item, parent, false);

        // Lookup view for data population
        TextView tvClubName = (TextView) convertView.findViewById(R.id.tvClubName);
        TextView tvClubIntro = (TextView) convertView.findViewById(R.id.tvClubIntro);
        final ImageView toggleStar = (ImageView) convertView.findViewById(R.id.ivToggleStar);
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final View finalConvertView = convertView;
        ImageUtil.getInstance().loadImage(getContext(), handler, club.getWallpaperLink(), new AfterImageLoadListener() {
            @Override
            public void setImage(Bitmap bitmap) {
                ImageView iv = finalConvertView.findViewById(R.id.ivClubWallpaper);
                ImageUtil.getInstance().setImage(iv, bitmap, false);
            }
        });

        // Populate the data into the template view using the data object
        tvClubName.setText(club.getName());
        tvClubIntro.setText(club.getIntroduction());

        if(club.isStarred(uid)){
            toggleStar.setImageResource(android.R.drawable.star_big_on);
        }
        else{
            toggleStar.setImageResource(android.R.drawable.star_big_off);
        }

        toggleStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(club.isStarred(uid)){
                    toggleStar.setImageResource(android.R.drawable.star_big_off);
                    ClubAPI.getInstance().toggleStarClub(club.getKey(), club, user, false);
                }
                else{
                    toggleStar.setImageResource(android.R.drawable.star_big_on);
                    ClubAPI.getInstance().toggleStarClub(club.getKey(), club, user, true);
                }
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }
}
