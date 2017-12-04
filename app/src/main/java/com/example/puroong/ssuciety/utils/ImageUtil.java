package com.example.puroong.ssuciety.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.puroong.ssuciety.listeners.AfterImageLoadListener;
import com.example.puroong.ssuciety.listeners.AfterImageUploadListener;
import com.example.puroong.ssuciety.listeners.AfterQueryListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by puroong on 11/26/17.
 */

public class ImageUtil {
    public static final int PICK_IMAGE = 999;
    private static ImageUtil instance;

    private ImageUtil() {}

    public static ImageUtil getInstance() {
        if(instance == null){
            instance = new ImageUtil();
        }

        return instance;
    }

    public void chooseImage(Activity activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    public void uploadImage(final Context context, Bitmap bitmap, String url, final AfterImageUploadListener listener) {
        File uploadFile = new File(url);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("/image/"+uploadFile.getName());

        UploadTask uploadTask = storageRef.putBytes(imageData);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                listener.afterImageUpload(downloadUrl.toString());
            }
        });
    }

    public void loadImage(final Context context, final Handler handler, final String url, final AfterImageLoadListener listener) {
        // define thread
        final Thread loadImage = new Thread("Load Image") {
            public void run() {
                try {
                    if(url != ""){
                        URL downloadUrl = new URL(url);
                        final Bitmap bitmap = BitmapFactory.decodeStream(downloadUrl.openConnection().getInputStream());

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.setImage(bitmap);
                            }
                        });
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
//                    Toast.makeText(context, "Image download url is malformed", Toast.LENGTH_SHORT).show();
                }
            }
        };

        // start thread
        loadImage.start();
    }

    public void setImage(ImageView iv, Bitmap bitmap, boolean shouldFill) {
        if(shouldFill){
            iv.setImageBitmap(Bitmap.createScaledBitmap(bitmap, iv.getWidth(), iv.getHeight(), false));
        } else {
            iv.setImageBitmap(bitmap);
        }
    }
}
