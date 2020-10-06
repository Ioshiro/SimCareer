package com.example.simcareer.ui.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.adapter.FragmentViewHolder;
import androidx.viewpager2.widget.ViewPager2;

import com.example.simcareer.R;
import com.example.simcareer.bean.Cup;
import com.example.simcareer.bean.CupSetting;
import com.example.simcareer.bean.DbManager;
import com.example.simcareer.bean.RaceSetting;
import com.example.simcareer.ui.cup.details.CupSettingRecyclerAdapter;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class HomeFragment extends Fragment implements CupSlideRecyclerAdapter.CupListener {

    private HomeViewModel homeViewModel;
    private static int NUM_PAGES = 0;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private CupSlideRecyclerAdapter adapter;
    static View root;
    private static final int IMAGE_ID[] = {
            R.drawable.logo_cup_0,
            R.drawable.logo_cup_1
    };




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_active_cup);
        recyclerView =  root.findViewById(R.id.pager_active_cup);
        recyclerView2 = root.findViewById(R.id.pager_past_cup);
        try {
            List<Cup> cups = DbManager.getCups(root.getContext());
             adapter = new CupSlideRecyclerAdapter(cups);
             adapter.registerListener(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView2.setAdapter(adapter);
        recyclerView2.setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.HORIZONTAL, false));

        // add pager behavior
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        PagerSnapHelper snapHelper2 = new PagerSnapHelper();
        snapHelper2.attachToRecyclerView(recyclerView2);

        // pager indicator
        recyclerView.addItemDecoration(new LinePagerIndicatorDecoration());
        recyclerView2.addItemDecoration(new LinePagerIndicatorDecoration());
        return root;
    }


    @Override
    public void onClick(int id) {
        navigateToCupFragment(id);
    }

    public void navigateToCupFragment(int id){
        HomeFragmentDirections.ActionOpenCup action = HomeFragmentDirections.actionOpenCup();
        action.setCupId(id);
        Navigation.findNavController(root).navigate(action);
    }

}