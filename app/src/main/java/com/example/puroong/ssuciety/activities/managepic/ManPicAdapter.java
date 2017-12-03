package com.example.puroong.ssuciety.activities.managepic;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.puroong.ssuciety.R;
import com.example.puroong.ssuciety.listeners.AfterImageLoadListener;
import com.example.puroong.ssuciety.models.ClubActivity;
import com.example.puroong.ssuciety.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by uhn61 on 2017-11-27.
 */

public class ManPicAdapter extends ArrayAdapter<ClubActivity> {
    private List<ClubActivity> clubActivities = new ArrayList<>();
    private Handler handler = new Handler();

    public ManPicAdapter(Context context, ArrayList<ClubActivity> clubActivities) {
        super(context, 0, clubActivities);
        this.clubActivities = clubActivities;

    }

    public void add(ClubActivity item) {
        clubActivities.add(item);
        notifyDataSetChanged();
    }

    public void updateByKey(String key, ClubActivity updated) {
        int index = 0;

        for(ClubActivity clubSchedule : clubActivities){
            if(key.equals(clubSchedule.getKey())){
                clubActivities.remove(clubSchedule);
                clubActivities.add(index, updated);
                notifyDataSetChanged();
                break;
            }
            index++;
        }
    }

    public void removeByKey(String key) {
        for(ClubActivity clubSchedule : clubActivities){
            if(key.equals(clubSchedule.getKey())){
                clubActivities.remove(clubSchedule);
                notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ClubActivity clubActivity = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.actpicview, parent, false);

        TextView tvname= (TextView) convertView.findViewById(R.id.actpicname);

        final View finalConvertView = convertView;
        ImageUtil.getInstance().loadImage(getContext(), handler, clubActivity.getPictureLink(), new AfterImageLoadListener() {
            @Override
            public void setImage(Bitmap bitmap) {
                ImageView iv = finalConvertView.findViewById(R.id.actpic);
                ImageUtil.getInstance().setImage(iv, bitmap, true);
            }
        });

        tvname.setText(clubActivity.getName());

        return convertView;
    }
}
