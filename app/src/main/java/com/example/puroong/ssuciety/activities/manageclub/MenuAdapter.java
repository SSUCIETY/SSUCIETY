package com.example.puroong.ssuciety.activities.manageclub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.puroong.ssuciety.R;

import java.util.ArrayList;

/**
 * Created by puroong on 11/23/17.
 */

public class MenuAdapter extends ArrayAdapter<String> {
    public MenuAdapter(Context context, ArrayList<String> menus) {super(context, 0, menus);}

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        String menuText =  getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_item, parent, false);
        }

        TextView menu = (TextView) convertView.findViewById(R.id.menu);
        menu.setText(menuText);

        return convertView;
    }
}