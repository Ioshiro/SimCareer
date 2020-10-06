package com.example.simcareer.ui.cup.details;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.simcareer.R;
import com.example.simcareer.bean.Cup;
import com.example.simcareer.bean.CupSetting;
import com.example.simcareer.bean.RaceDate;

import java.util.ArrayList;
import java.util.List;

public class CupCalendarRecyclerAdapter extends RecyclerView.Adapter<CupCalendarRecyclerAdapter.ViewHolder>{
    private final CupSetting cup;

    public CupCalendarRecyclerAdapter(CupSetting cup) {
        this.cup = cup;
    }

    @androidx.annotation.NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_cup_date, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public int getItemCount() {
        return cup.getRaceDates().size();
    }

    public void onBindViewHolder(final CupCalendarRecyclerAdapter.ViewHolder holder, int position) {
        List<RaceDate> raceDates = cup.getRaceDates();
        holder.raceDate = raceDates.get(position);
        holder.textSeq.setText(holder.raceDate.getSeq()+"");
        holder.textCircuit.setText(holder.raceDate.getCircuit());
        holder.textDate.setText(holder.raceDate.getDate());
        if(holder.raceDate.isDatePast())
            holder.framePast.setBackgroundResource(R.color.colorPrimary);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textSeq;
        public final TextView textCircuit;
        public final TextView textDate;
        public final FrameLayout framePast;
        public RaceDate raceDate;

        public ViewHolder(View view) {
            super(view);
            this.mView = view;
            this.textSeq = view.findViewById(R.id.text_seq);
            this.textCircuit = view.findViewById(R.id.text_circuit);
            this.textDate = view.findViewById(R.id.text_date);
            this.framePast = view.findViewById(R.id.frame_is_past);
        }

        @androidx.annotation.NonNull
        @Override
        public String toString() {
            return super.toString();
        }
    }
}
