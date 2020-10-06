package com.example.simcareer.ui.gallery;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simcareer.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GalleryFragment extends Fragment implements PhotoRecyclerAdapter.PhotoListener {

    private GalleryViewModel galleryViewModel;
    RecyclerView recyclerView;
    List<Integer> photoIds = new ArrayList<>(Arrays.asList(
            R.drawable.gte1, R.drawable.gte2, R.drawable.gte3, R.drawable.gte4, R.drawable.gte5, R.drawable.gte6, R.drawable.gte7, R.drawable.gte8, R.drawable.gte9, R.drawable.gte10,
            R.drawable.gte11, R.drawable.gte12, R.drawable.gte14, R.drawable.gte15, R.drawable.gte16, R.drawable.gte18, R.drawable.gte19, R.drawable.gte20
    ));
    List<Integer> fullPhotoIds = new ArrayList<>(Arrays.asList(
            R.drawable.big_gte1, R.drawable.big_gte2, R.drawable.big_gte3, R.drawable.big_gte4, R.drawable.big_gte5, R.drawable.big_gte6, R.drawable.big_gte7, R.drawable.big_gte8, R.drawable.big_gte9, R.drawable.big_gte10,
            R.drawable.big_gte11, R.drawable.big_gte12, R.drawable.big_gte14, R.drawable.big_gte15, R.drawable.big_gte16, R.drawable.big_gte18, R.drawable.big_gte19, R.drawable.big_gte20
    ));


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        recyclerView = root.findViewById(R.id.recycler_gallery);
        PhotoRecyclerAdapter adapter = new PhotoRecyclerAdapter(photoIds, fullPhotoIds);
        adapter.registerListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        recyclerView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onSelect(int id) {
        showImage(id);
    }

    public void showImage(int id) {
        FragmentManager fm = getParentFragmentManager();
        PhotoDialogActivity frag = new PhotoDialogActivity(id);
        frag.show(fm , "photo");
    }

}