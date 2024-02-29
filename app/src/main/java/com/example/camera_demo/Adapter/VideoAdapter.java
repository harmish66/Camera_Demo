package com.example.camera_demo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.camera_demo.R;

import java.io.File;

public class VideoAdapter extends  RecyclerView.Adapter<VideoAdapter.VidHolder> {
    private Context videoContext;
    File[] files;
    public VideoAdapter(Context videoContext, File[] files) {
        this.videoContext = videoContext;
        this.files = files;
    }

    @NonNull
    @Override
    public VideoAdapter.VidHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View cell = inflater.inflate(R.layout.video_holder_item, parent, false);
        return new VidHolder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.VidHolder holder, int position) {

        Glide.with(videoContext)
                .load(files[holder.getAdapterPosition()].getAbsoluteFile())
                .apply(new RequestOptions().centerCrop())
                .into(holder.video);
    }


    @Override
    public int getItemCount() {
        return files.length;
    }

    public class VidHolder extends RecyclerView.ViewHolder {

        ImageView video;
        public VidHolder(@NonNull View itemView) {
            super(itemView);
            video = itemView.findViewById(R.id.video);
        }
    }
}
