package com.example.camera_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class ImageShowActivity extends AppCompatActivity {

    ImageView image;
    String file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_shoe);

        image = findViewById(R.id.image);
        file = getIntent().getStringExtra("imageFile");
        Glide.with(this).load(file).into(image);

    }
}