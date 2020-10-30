package com.example.simcareer.ui.subscriptions;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.simcareer.R;
import com.example.simcareer.bean.CupSetting;
import com.example.simcareer.bean.DbManager;
import com.example.simcareer.bean.User;

import org.json.JSONException;

import java.util.List;

public class SubscriptionFragment extends Fragment {

    private SubscriptionViewModel mViewModel;
    View root;
    RecyclerView recyclerSubs;
    TextView textEmpty;

    public static SubscriptionFragment newInstance() {
        return new SubscriptionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root =inflater.inflate(R.layout.fragment_subscription, container, false);
        recyclerSubs = root.findViewById(R.id.recycler_subs);
        textEmpty = root.findViewById(R.id.text_empty);
        int id = requireActivity().getIntent().getIntExtra("id", -1);
        User user = null;
        try {
            user = DbManager.getFullUserById(requireContext(), id);
            List<CupSetting> cups = DbManager.getSubscribedCups(requireContext(), user.getName());
            SubscriptionRecyclerAdapter adapter = new SubscriptionRecyclerAdapter(cups, user.getName(), textEmpty);
            recyclerSubs.setLayoutManager(new LinearLayoutManager(requireContext()));
            recyclerSubs.setAdapter(adapter);

            if (cups.isEmpty()){
                recyclerSubs.setVisibility(View.GONE);
                textEmpty.setVisibility(View.VISIBLE);
            } else {
                recyclerSubs.setVisibility(View.VISIBLE);
                textEmpty.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SubscriptionViewModel.class);
        // TODO: Use the ViewModel
    }

}