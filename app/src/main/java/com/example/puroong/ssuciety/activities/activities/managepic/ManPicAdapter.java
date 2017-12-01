package com.example.puroong.ssuciety.activities.activities.managepic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.puroong.ssuciety.R;
import com.example.puroong.ssuciety.models.ClubActivity;

import java.net.URI;
import java.util.ArrayList;

/**
 * Created by uhn61 on 2017-11-27.
 */

public class ManPicAdapter extends ArrayAdapter<ClubActivity> {
    public ManPicAdapter(Context context, ArrayList<ClubActivity> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ClubActivity user = getItem(position);
        URI uri = new URI(user.getClubId());
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.actpicview, parent, false);
        }
        TextView tvname= (TextView) convertView.findViewById(R.id.actpicname);
        ImageView ivname=(ImageView) convertView.findViewById(R.id.actpic);
        tvname.setText(user.getName());
        ivname.setImageURI();
        return convertView;
    }
}
