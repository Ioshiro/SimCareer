package com.example.simcareer.ui.cup;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.simcareer.R;
import com.example.simcareer.bean.CupSetting;
import com.example.simcareer.bean.DbManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CupSubscribeDialog extends DialogFragment {
    private CupSubscribeDialog mViewModel;
    private View root;
    public static CupSetting cupSetting;
    TextInputLayout inputTeam;
    TextInputLayout inputCar;
    CupListener listener;

    public interface CupListener{
        void onSub(String car, String team);
    }

    public void registerListener(CupListener listener){
        this.listener = listener;
    }

    public static CupSubscribeDialog newInstance(CupSetting cupSetting) throws JSONException {
        CupSubscribeDialog fragment = new CupSubscribeDialog();
        Bundle bundle = new Bundle(cupSetting.toBundle());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_cup_subscribe_dialog, container, false);
        return root;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        root = inflater.inflate(R.layout.fragment_cup_subscribe_dialog, null);
        Bundle bundle = getArguments();
        String cars = bundle.getString("lista-auto");
        inputTeam = root.findViewById(R.id.text_input_team);
        inputCar = root.findViewById(R.id.text_input_car);
        try {
            List<String> teams = DbManager.getTeams(requireContext());
            ((AutoCompleteTextView)inputTeam.getEditText()).setAdapter(new ArrayAdapter<>(requireContext(), R.layout.list_item, teams));
            String[] elements = cars.split(",");
            List<String> carList = Arrays.asList(elements);
            ((AutoCompleteTextView)inputCar.getEditText()).setAdapter(new ArrayAdapter<>(requireContext(), R.layout.list_item, carList));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setTitle("Iscrizione campionato");

        builder.setView(root)
                // Add action buttons
                .setPositiveButton("Iscrivimi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onSub(inputCar.getEditText().getText().toString(),
                                        inputTeam.getEditText().getText().toString());
                        getDialog().cancel();
                    }
                })
                .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getDialog().cancel();
                    }
                });
        final AlertDialog dialog = builder.create();
        return dialog;
    }
}
