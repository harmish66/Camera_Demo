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
import com.example.camera_demo.Adapter.VideoAdapter;

import java.io.File;

public class VideoFragment extends Fragment {

    RecyclerView folderRecycler;
    Context videoContext;
    String videopath;
    File file;

    public VideoFragment(Context context) {
        // Required empty public constructor
        this.videoContext = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        folderRecycler = view.findViewById(R.id.folderRecyclervid);
        folderRecycler.hasFixedSize();


        videopath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + getResources().getString(R.string.app_name) + "/" + "Videos";
        this.file = new File(videopath);



        folderRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        folderRecycler.setHasFixedSize(true);
        RecyclerView.Adapter folderAdapter = new VideoAdapter(videoContext,this.file.listFiles());
        folderRecycler.setAdapter(folderAdapter);

    }

}