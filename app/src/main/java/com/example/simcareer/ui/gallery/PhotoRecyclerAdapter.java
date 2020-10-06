package com.example.simcareer.ui.gallery;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simcareer.R;
import com.example.simcareer.bean.RaceDate;
import com.example.simcareer.ui.cup.details.CupCalendarRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PhotoRecyclerAdapter extends RecyclerView.Adapter<PhotoRecyclerAdapter.ViewHolder> {
    List<Integer> photoIds;
    List<Integer> fullPhotoIds;
    View root;
    PhotoListener listener;
    public PhotoRecyclerAdapter(List<Integer> photos, List<Integer> fulls){
        photoIds = photos;
        fullPhotoIds = fulls;
    }

    public interface PhotoListener{
        void onSelect(int id);
    }

    public void registerListener(PhotoListener listener){
        this.listener = listener;
    }

    @androidx.annotation.NonNull
    @Override
    public PhotoRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_gallery_photo, parent, false);
        return new PhotoRecyclerAdapter.ViewHolder(root);
    }

    @Override
    public int getItemCount() {
        return photoIds.size();
    }


    public void onBindViewHolder(final PhotoRecyclerAdapter.ViewHolder holder, int position) {
        holder.photoId = photoIds.get(position);
        holder.fullPhotoId = fullPhotoIds.get(position);
        holder.imagePhoto.setImageResource(holder.photoId);
        holder.imagePhoto.setClickable(true);
        holder.imagePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSelect(holder.fullPhotoId);
            }
        });
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView imagePhoto;
        public int photoId;
        public int fullPhotoId;

        public ViewHolder(View view) {
            super(view);
            this.mView = view;
            this.imagePhoto = view.findViewById(R.id.image_photo);
        }

        @androidx.annotation.NonNull
        @Override
        public String toString() {
            return super.toString();
        }
    }


}
