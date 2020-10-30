package com.example.simcareer.ui.subscriptions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.simcareer.R;
import com.example.simcareer.bean.CupSetting;
import com.example.simcareer.bean.DbManager;
import com.example.simcareer.bean.Pilot;
import com.example.simcareer.bean.PilotPoints;
import com.example.simcareer.bean.RaceDate;
import com.example.simcareer.ui.cup.details.CupCalendarRecyclerAdapter;

import org.json.JSONException;

import java.util.List;

public class SubscriptionRecyclerAdapter extends RecyclerView.Adapter<SubscriptionRecyclerAdapter.ViewHolder> {
    private final List<CupSetting> cups;
    String pilotName;
    TextView textEmpty;
    View root;

    public SubscriptionRecyclerAdapter(List<CupSetting> cups, String pilot, TextView textEmpty) {
        this.cups = cups;
        this.pilotName = pilot;
        this.textEmpty = textEmpty;
    }

    @androidx.annotation.NonNull
    @Override
    public SubscriptionRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_subscription_item, parent, false);
        return new SubscriptionRecyclerAdapter.ViewHolder(root);
    }



    @Override
    public int getItemCount() {
        return cups.size();
    }


    public void onBindViewHolder(final SubscriptionRecyclerAdapter.ViewHolder holder, final int position) {
        holder.cup = cups.get(position);
        int num = holder.cup.countPastDates();
        holder.textRounds.setText("R"+num+"/"+holder.cup.getRaceDates().size());
        holder.textCupName.setText(holder.cup.getName());
        int points = 0;
        try {
            points = DbManager.getCupLeaderboard(holder.mView.getContext(), holder.cup.getId()).getPilotPoints(pilotName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.textScore.setText("Score:"+points);
        if(num == holder.cup.getRaceDates().size())
            holder.framePast.setBackgroundResource(R.color.colorPrimary);
        holder.imageUnsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    boolean isRemoved = DbManager.removeCupPilot(holder.mView.getContext(), pilotName, holder.cup.getId());
                    if(isRemoved){
                        cups.remove(position);
                        notifyDataSetChanged();
                        if(cups.isEmpty()){
                            root.setVisibility(View.GONE);
                            textEmpty.setVisibility(View.VISIBLE);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }




    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textRounds;
        public final TextView textCupName;
        public final TextView textScore;
        public final FrameLayout framePast;
        public final ImageView imageUnsub;
        public CupSetting cup;

        public ViewHolder(View view) {
            super(view);
            this.mView = view;
            this.textRounds = view.findViewById(R.id.text_rounds);
            this.textCupName = view.findViewById(R.id.text_cup_name);
            this.textScore = view.findViewById(R.id.text_score);
            this.framePast = view.findViewById(R.id.frame_is_past);
            this.imageUnsub = view.findViewById(R.id.image_unsub);
        }

        @androidx.annotation.NonNull
        @Override
        public String toString() {
            return super.toString();
        }
    }

}
