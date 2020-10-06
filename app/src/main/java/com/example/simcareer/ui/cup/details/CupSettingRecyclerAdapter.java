package com.example.simcareer.ui.cup.details;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.simcareer.R;
import com.example.simcareer.bean.CupSetting;
import com.example.simcareer.bean.RaceSetting;

import java.util.List;

public class CupSettingRecyclerAdapter extends RecyclerView.Adapter<CupSettingRecyclerAdapter.ViewHolder>{
    private final CupSetting cup;

    public CupSettingRecyclerAdapter(CupSetting cup) {
        this.cup = cup;
    }

    @androidx.annotation.NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_cup_setting, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public int getItemCount() {
        return cup.getRaceSettings().size();
    }

    public void onBindViewHolder(final CupSettingRecyclerAdapter.ViewHolder holder, int position) {
        List<RaceSetting> raceSettings = cup.getRaceSettings();
        holder.raceSetting = raceSettings.get(position);
        holder.textSetting.setText(holder.raceSetting.getType());
        holder.textValue.setText(holder.raceSetting.getValue());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textSetting;
        public final TextView textValue;
        public RaceSetting raceSetting;

        public ViewHolder(View view) {
            super(view);
            this.mView = view;
            this.textSetting = view.findViewById(R.id.text_setting);
            this.textValue = view.findViewById(R.id.text_value);
        }

        @androidx.annotation.NonNull
        @Override
        public String toString() {
            return super.toString();
        }
    }
}

