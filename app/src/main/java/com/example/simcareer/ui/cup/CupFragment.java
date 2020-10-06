package com.example.simcareer.ui.cup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.simcareer.MainNavActivity;
import com.example.simcareer.R;
import com.example.simcareer.bean.Cup;
import com.example.simcareer.bean.CupSetting;
import com.example.simcareer.bean.DbManager;
import com.example.simcareer.bean.Pilot;
import com.example.simcareer.bean.User;
import com.example.simcareer.ui.cup.details.CupCalendarRecyclerAdapter;
import com.example.simcareer.ui.cup.details.CupPilotRecyclerAdapter;
import com.example.simcareer.ui.cup.details.CupSettingRecyclerAdapter;
import com.example.simcareer.ui.home.HomeFragmentDirections;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;

public class CupFragment extends Fragment implements CupSubscribeDialog.CupListener {

    private CupViewModel mViewModel;
    private static CupSetting cup;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    TabLayout tabLayout;
    static View root;
    int userId;
    static int id;
    FloatingActionButton fabSubscribe;
    boolean isPilotSubscribed;
    User user;

    public static CupFragment newInstance() {
        return new CupFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_cup, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CupViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tabLayout = view.findViewById(R.id.tabs);
        recyclerView = view.findViewById(R.id.recycler_cup);
        fabSubscribe = view.findViewById(R.id.floating_action_button);
        userId = requireActivity().getIntent().getIntExtra("id", -1);
        try {
            user = DbManager.getFullUserById(requireContext(), userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try {
                    setRecycler(tab.getPosition(),cup);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                try {
                    setRecycler(tab.getPosition(), cup);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        fabSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPilotSubscribed){
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
                    builder.setTitle("Disiscrizione");
                    builder.setMessage("Vuoi disiscriverti da questo campionato? Perderai tutti i punti!");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                DbManager.removeCupPilot(requireContext(), user.getName(), id);
                                fabSubscribe.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                                fabSubscribe.setImageResource(R.drawable.ic_plus_outline_24);
                                isPilotSubscribed = false;
                                if(tabLayout.getSelectedTabPosition() == 1){
                                    setRecycler(1, cup);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
                else{
                    //dialog subscribe
                    try {
                        FragmentManager fm = getParentFragmentManager();
                        CupSubscribeDialog dialog = CupSubscribeDialog.newInstance(cup);
                        dialog.registerListener(CupFragment.this);
                        dialog.show(fm, "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        MainNavActivity.showIcon();
        id = CupFragmentArgs.fromBundle(getArguments()).getCupId();
        Log.d("CupFragment.OnResume", "recieved id"+id);
        try {
            cup = DbManager.getCupSetting(root.getContext(), id);
            actionBar.setTitle(cup.getName());
            setRecycler(0, cup);
            isPilotSubscribed = DbManager.isPilotSubscribedToCup(requireContext(), user.getName(), id);
            if(isPilotSubscribed) {
                fabSubscribe.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                fabSubscribe.setImageResource(R.drawable.ic_remove_24);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        MainNavActivity.hideIcon();
    }
    public void setRecycler(int type, CupSetting cup) throws JSONException {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        cup = DbManager.getCupSetting(requireContext(), id);
        recyclerView.setLayoutManager(layoutManager);
        if(type == 0)
            adapter = new CupCalendarRecyclerAdapter(cup);
        if(type == 1)
            adapter = new CupPilotRecyclerAdapter(cup);
        if(type == 2)
            adapter = new CupSettingRecyclerAdapter(cup);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        Log.d("setRecycler list size:",""+adapter.getItemCount());
    }

    @Override
    public void onSub(String car, String team) {
        try {
            User user = DbManager.getFullUserById(requireContext(), userId);
            Pilot newPilot = new Pilot(user.getName(), team, car);
            boolean isUpdated = DbManager.addCupPilot(requireContext(), newPilot, id);
            Log.d("onSub", "added pilot new size: "+cup.getPilotsSubscribed().toString());
            if(isUpdated){
                fabSubscribe.hide();
                fabSubscribe.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                fabSubscribe.setImageResource(R.drawable.ic_remove_24);
                fabSubscribe.show();
                isPilotSubscribed = true;
                if(tabLayout.getSelectedTabPosition() == 1){
                    setRecycler(1, cup);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_nav_cup_to_nav_leaderboard:
                CupFragmentDirections.ActionNavCupToNavLeaderboard action = CupFragmentDirections.actionNavCupToNavLeaderboard();
                action.setId(id);
                Navigation.findNavController(root).navigate(action);
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void navigateToLeaderboards(){
        CupFragmentDirections.ActionNavCupToNavLeaderboard action = CupFragmentDirections.actionNavCupToNavLeaderboard();
        action.setId(id);
        Navigation.findNavController(root).navigate(action);
    }
}