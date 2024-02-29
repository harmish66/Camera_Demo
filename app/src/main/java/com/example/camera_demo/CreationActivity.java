package com.example.camera_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;

import com.example.camera_demo.Adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class CreationActivity extends AppCompatActivity {
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);

        context =  this;
        viewPager = findViewById(R.id.viewpager);


        viewPagerAdapter =  new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.add(new ImageFragment(context),"Images");
        viewPagerAdapter.add(new VideoFragment(context),"Videos");
        //viewPagerAdapter.add(new Temp(context),"Temp");

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout =  findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

    }
}