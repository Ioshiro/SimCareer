package com.example.simcareer.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.simcareer.R;
import com.example.simcareer.bean.Cup;
import com.example.simcareer.bean.CupSetting;
import com.example.simcareer.bean.DbManager;
import com.example.simcareer.bean.RaceSetting;

import java.io.IOException;
import java.util.List;

public class CupSlideRecyclerAdapter extends RecyclerView.Adapter<CupSlideRecyclerAdapter.ViewHolder>{
    private final List<Cup> cups;
    CupListener listener;

    public CupSlideRecyclerAdapter(List<Cup> cups) {
        this.cups = cups;
    }

    public interface CupListener{
        void onClick(int id);
    }

    public void registerListener(CupListener listener){
        this.listener = listener;
    }

    @androidx.annotation.NonNull
    @Override
    public CupSlideRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_cup_slide_page, parent, false);
        return new CupSlideRecyclerAdapter.ViewHolder(root);
    }

    @Override
    public int getItemCount() {
        return cups.size();
    }

    public void onBindViewHolder(final CupSlideRecyclerAdapter.ViewHolder holder, int position) {
        holder.cup = cups.get(position);
        try {
            holder.imageCup.setImageDrawable(DbManager.loadDrawableFromAsset(holder.mView.getContext(), holder.cup.getLogo()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.imageCup.setClickable(true);
        holder.imageCup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(holder.cup.getId());
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView imageCup;
        public Cup cup;

        public ViewHolder(View view) {
            super(view);
            this.mView = view;
            this.imageCup = view.findViewById(R.id.text_cup_name);

        }

        @androidx.annotation.NonNull
        @Override
        public String toString() {
            return super.toString();
        }
    }
}


