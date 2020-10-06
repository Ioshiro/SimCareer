package com.example.simcareer.ui.cup.details;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.simcareer.R;
import com.example.simcareer.bean.CupSetting;
import com.example.simcareer.bean.Pilot;
import com.example.simcareer.bean.RaceDate;

import java.util.List;

public class CupPilotRecyclerAdapter extends RecyclerView.Adapter<CupPilotRecyclerAdapter.ViewHolder>{
    private final CupSetting cup;

    public CupPilotRecyclerAdapter(CupSetting cup) {
        this.cup = cup;
    }

    @androidx.annotation.NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_cup_pilot, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public int getItemCount() {
        return cup.getPilotsSubscribed().size();
    }

    public void onBindViewHolder(final CupPilotRecyclerAdapter.ViewHolder holder, int position) {
        List<Pilot> pilots = cup.getPilotsSubscribed();
        holder.pilot = pilots.get(position);
        holder.textPosition.setText(""+(position+1));
        holder.textName.setText(holder.pilot.getName());
        holder.textTeam.setText(holder.pilot.getTeam());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView imagePropic;
        public final TextView textPosition;
        public final TextView textName;
        public final TextView textTeam;
        public Pilot pilot;

        public ViewHolder(View view) {
            super(view);
            this.mView = view;
            this.imagePropic = view.findViewById(R.id.image_propic);
            this.textPosition = view.findViewById(R.id.text_position);
            this.textName = view.findViewById(R.id.text_name);
            this.textTeam = view.findViewById(R.id.text_team);
        }

        @androidx.annotation.NonNull
        @Override
        public String toString() {
            return super.toString();
        }
    }
}

