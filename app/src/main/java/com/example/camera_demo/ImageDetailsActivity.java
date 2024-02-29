package com.example.camera_demo;


import static com.example.camera_demo.ImageFragment.allimagesPath;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ImageDetailsActivity extends AppCompatActivity {

    String string;
    int postion;
    ImageView image;
    ViewPager imagePager;
    ImagesPagerAdapter pagingImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_dtails);
        imagePager = findViewById(R.id.imagePager);

        pagingImages = new ImagesPagerAdapter();
        imagePager.setAdapter(pagingImages);

        imagePager.setCurrentItem(postion);

        postion = getIntent().getIntExtra("position",0);

        string = getIntent().getStringExtra("path");

        Log.d("TAG2020", "onCreate: " + allimagesPath.get(postion));
        Log.d("TAG2020", "onCreate: " + postion);

    }

    private class ImagesPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 0;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup containerCollection, int position) {
            LayoutInflater layoutinflater = (LayoutInflater) containerCollection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutinflater.inflate(R.layout.picture_browser_pager, null);
            image = view.findViewById(R.id.image);


            //setTransitionName(image, String.valueOf(position) + "picture");
//            pictureFacer pic = allImages.get(position);

            Glide.with(ImageDetailsActivity.this)
                    .load(allimagesPath.get(position))
                    .apply(new RequestOptions().fitCenter())
                    .into(image);
            ((ViewPager) containerCollection).addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup containerCollection, int position, Object view) {
            ((ViewPager) containerCollection).removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == ((View) object);

        }
    }
}