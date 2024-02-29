package com.example.camera_demo.Adapter;

import static com.example.camera_demo.ImageFragment.allimagesPath;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.camera_demo.ImageDetailsActivity;
import com.example.camera_demo.R;

import java.io.File;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.PicHolder> {
    private Context folderContx;
    private File[] file;

    public ImageAdapter(Context folderContx , File[] file) {

        this.folderContx = folderContx;
        this.file =  file;

        //Log.d("Size_folders","" + folders.size());
    }




    @NonNull
    @Override
    public PicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View cell = inflater.inflate(R.layout.pic_holder_item, parent, false);
        return new PicHolder(cell);

    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.PicHolder holder, int position) {

//        final pictureFacer image = pictureList.get(position);

        Glide.with(folderContx)
                .load(file[holder.getAdapterPosition()].getAbsoluteFile())
                .apply(new RequestOptions().centerCrop())
                .into(holder.picture);

        holder.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(folderContx, ImageDetailsActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("path", file[holder.getAdapterPosition()].getAbsoluteFile().getPath());
                folderContx.startActivity(intent);

            }
        });
    }




    @Override
    public int getItemCount() {
        return file.length;
    }




    public class PicHolder extends RecyclerView.ViewHolder {
        public ImageView picture;


        public PicHolder(@NonNull View itemView) {
            super(itemView);


            picture = itemView.findViewById(R.id.image);

        }
    }

}


