package com.example.puroong.ssuciety.activities;

import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.puroong.ssuciety.R;
import com.example.puroong.ssuciety.api.UserAPI;
import com.example.puroong.ssuciety.listeners.AfterImageLoadListener;
import com.example.puroong.ssuciety.models.User;
import com.example.puroong.ssuciety.utils.ImageUtil;

public class profile_see extends AppCompatActivity {
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_see);

        User user = UserAPI.getInstance().getCurrentUser();

        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvMajor = (TextView) findViewById(R.id.tvMajor);
        TextView tvYear = (TextView) findViewById(R.id.tvYear);
        TextView tvContact = (TextView) findViewById(R.id.tvContact);

        ImageUtil.getInstance().loadImage(getApplicationContext(), handler, user.getProfileLink(), new AfterImageLoadListener() {
            @Override
            public void setImage(Bitmap bitmap) {
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                ImageUtil.getInstance().setImage(imageView, bitmap, true);
            }
        });

        tvName.setText(user.getName());
        tvMajor.setText(user.getMajor());
        tvYear.setText(Integer.toString(user.getAdmissionYear()));
        tvContact.setText(user.getPhoneNumber());
    }
}
