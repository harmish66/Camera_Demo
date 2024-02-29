package com.example.camera_demo;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.camera_demo.Adapter.ImageAdapter;

import java.io.File;
import java.util.ArrayList;


public class ImageFragment extends Fragment {


    Context imageContext;
    RecyclerView folderRecycler;
    File file;
    ArrayList<String> path;
    public static ArrayList<String[]> allimagesPath = new ArrayList<>();

    String photopath;

    public ImageFragment(Context context) {
        this.imageContext = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image, container, false);

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        folderRecycler = view.findViewById(R.id.folderRecycler);
        folderRecycler.hasFixedSize();


        photopath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + getResources().getString(R.string.app_name) + "/" + "Photos";
        this.file = new File(photopath);


        Log.d("TAG1010", "onViewCreated: "+photopath.getBytes().toString());

        folderRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        folderRecycler.setHasFixedSize(true);
        RecyclerView.Adapter folderAdapter = new ImageAdapter(imageContext, this.file.listFiles());
        folderRecycler.setAdapter(folderAdapter);

        for (int i = 0; i < this.file.getAbsoluteFile().list().length; i++) {
            allimagesPath.add(this.file.getAbsoluteFile().list());
        }

    }


}