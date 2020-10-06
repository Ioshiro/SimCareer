package com.example.simcareer.ui.leaderboard;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.simcareer.R;
import com.example.simcareer.bean.CupLeaderboard;
import com.example.simcareer.bean.CupSetting;
import com.example.simcareer.bean.DbManager;
import com.example.simcareer.bean.User;
import com.example.simcareer.ui.cup.CupFragment;
import com.example.simcareer.ui.cup.CupFragmentArgs;
import com.example.simcareer.ui.cup.CupSubscribeDialog;
import com.example.simcareer.ui.cup.details.CupCalendarRecyclerAdapter;
import com.example.simcareer.ui.cup.details.CupPilotRecyclerAdapter;
import com.example.simcareer.ui.cup.details.CupSettingRecyclerAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;

public class LeaderboardFragment extends Fragment {

    private LeaderboardViewModel mViewModel;
    TabLayout tabLayout;
    RecyclerView recyclerView;
    int userId;
    int id;
    User user;
    LeaderboardRecyclerAdapter adapter;

    public static LeaderboardFragment newInstance() {
        return new LeaderboardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_leaderboard, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LeaderboardViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tabLayout = view.findViewById(R.id.tabs);
        recyclerView = view.findViewById(R.id.recycler_leaderboard);
        userId = requireActivity().getIntent().getIntExtra("id", -1);

        try {
            user = DbManager.getFullUserById(requireContext(), userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0) {
                    try {
                        setRecycler(true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        setRecycler(false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // setRecycler(tab.getPosition(), cup);
            }
        });
    }

    public void onStart(){
        super.onStart();
        id = LeaderboardFragmentArgs.fromBundle(getArguments()).getId();
        try {
            setRecycler(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setRecycler(boolean isPilot) throws JSONException {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        CupLeaderboard cup = DbManager.getCupLeaderboard(requireContext(), id);
        recyclerView.setLayoutManager(layoutManager);
        if(isPilot)
            adapter = new LeaderboardRecyclerAdapter(cup, isPilot);
        else
            adapter = new LeaderboardRecyclerAdapter(cup, isPilot);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

}