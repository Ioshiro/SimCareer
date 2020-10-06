package com.example.simcareer.ui.leaderboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.simcareer.R;
import com.example.simcareer.bean.CupLeaderboard;
import com.example.simcareer.bean.PilotPoints;
import com.example.simcareer.bean.RaceDate;
import com.example.simcareer.bean.Team;
import com.example.simcareer.bean.TeamPoints;
import com.example.simcareer.ui.cup.details.CupCalendarRecyclerAdapter;

import java.util.List;

public class LeaderboardRecyclerAdapter extends RecyclerView.Adapter<LeaderboardRecyclerAdapter.ViewHolder> {
    CupLeaderboard cup;
    boolean isPilot;

    public LeaderboardRecyclerAdapter(CupLeaderboard cup, boolean isPilot){
        this.cup = cup;
        this.isPilot = isPilot;
    }

    @Override
    public int getItemCount() {
        if(isPilot)
            return cup.getPilotsPoints().size();
        else
            return cup.getTeamsPoints().size();
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(isPilot)
            holder.pilotPoints = cup.getPilotsPoints().get(position);
        if(!isPilot)
            holder.teamPoints = cup.getTeamsPoints().get(position);
        holder.textPos.setText(""+(position+1));
        if(isPilot) {
            holder.textName.setText(holder.pilotPoints.getName());
            holder.textSubName.setText(holder.pilotPoints.getTeam());
            holder.textPoints.setText(""+holder.pilotPoints.getPoints());
        }
        else {
            holder.textName.setText(holder.teamPoints.getName());
            holder.textSubName.setText(holder.teamPoints.getCar());
            holder.textPoints.setText(""+holder.teamPoints.getPoints());
        }
    }


    @androidx.annotation.NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_leaderboard_item, parent, false);
        return new ViewHolder(root);
    }




    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textPos;
        public final TextView textName;
        public final TextView textSubName;
        public final TextView textPoints;
        public final ImageView imagePropic;
        public final ImageView imageScore;
        public PilotPoints pilotPoints;
        public TeamPoints teamPoints;

        public ViewHolder(View view) {
            super(view);
            this.mView = view;
            this.textPos = view.findViewById(R.id.text_position);
            this.textName = view.findViewById(R.id.text_name);
            this.textSubName = view.findViewById(R.id.text_team);
            this.textPoints = view.findViewById(R.id.text_score);
            this.imagePropic = view.findViewById(R.id.image_propic);
            this.imageScore = view.findViewById(R.id.image_score);
        }

        @androidx.annotation.NonNull
        @Override
        public String toString() {
            return super.toString();
        }
    }


}
