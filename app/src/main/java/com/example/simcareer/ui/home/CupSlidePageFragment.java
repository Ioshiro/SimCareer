package com.example.simcareer.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentViewHolder;

import com.example.simcareer.R;
import com.example.simcareer.bean.Cup;
import com.example.simcareer.bean.DbManager;

import java.io.IOException;

public class CupSlidePageFragment extends Fragment {
    ViewGroup root;
    CupListener listener;
    public static int cupId;
    public static String cupName;
    public static String cupLogo;
    Cup cup;

    public CupSlidePageFragment(Cup cup){
        cupId = cup.getId();
        cupName = cup.getName();
        cupLogo = cup.getLogo();
    }

    public interface CupListener{
        void onClick(int id);
    }
    public void registerListener(CupListener listener){
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(
                R.layout.fragment_cup_slide_page, container, false);
        try {
            decorateView();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }

    public void decorateView() throws IOException {
        ImageView view = (ImageView) root.findViewById(R.id.text_cup_name);
        view.setContentDescription(cupName);
        view.setImageDrawable(DbManager.loadDrawableFromAsset(root.getContext(), cupLogo));
        view.setClickable(true);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(cupId);
            }
        });

    }
}
