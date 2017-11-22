package com.example.puroong.ssuciety.activities.clubschedulemanage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.puroong.ssuciety.R;
import com.example.puroong.ssuciety.api.ClubAPI;
import com.example.puroong.ssuciety.api.UserAPI;
import com.example.puroong.ssuciety.models.Club;
import com.example.puroong.ssuciety.models.ClubSchedule;
import com.example.puroong.ssuciety.models.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by puroong on 11/23/17.
 */

public class ClubScheduleManageAdapter extends ArrayAdapter<ClubSchedule> {
    private List<ClubSchedule> clubSchedules = new ArrayList<>();

    public ClubScheduleManageAdapter(Context context, ArrayList<ClubSchedule> clubSchedules) {
        super(context, 0, clubSchedules);
        this.clubSchedules = clubSchedules;
    }

    public void add(ClubSchedule item) {
        clubSchedules.add(item);
        notifyDataSetChanged();
    }

    public void updateByKey(String key, ClubSchedule updated) {
        int index = 0;

        for(ClubSchedule clubSchedule : clubSchedules){
            if(key.equals(clubSchedule.getKey())){
                clubSchedules.remove(clubSchedule);
                clubSchedules.add(index, updated);
                notifyDataSetChanged();
                break;
            }
            index++;
        }
    }

    public void removeByKey(String key) {
        for(ClubSchedule clubSchedule : clubSchedules){
            if(key.equals(clubSchedule.getKey())){
                clubSchedules.remove(clubSchedule);
                notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final User user = UserAPI.getInstance().getCurrentUser();

        // Get the data item for this position
        final ClubSchedule clubSchedule = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.club_schedule_manage_item, parent, false);
        }
        // Lookup view for data population
        TextView title = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView startDate = (TextView) convertView.findViewById(R.id.tvStartDate);
        TextView endDate = (TextView) convertView.findViewById(R.id.tvEndDate);

        // Populate the data into the template view using the data object
        title.setText(clubSchedule.getTitle());
        startDate.setText(clubSchedule.getStartDate());
        endDate.setText(clubSchedule.getEndDate());

        // Return the completed view to render on screen
        return convertView;
    }
}
