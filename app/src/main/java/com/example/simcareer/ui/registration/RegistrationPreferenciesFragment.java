package com.example.simcareer.ui.registration;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.simcareer.MainNavActivity;
import com.example.simcareer.R;
import com.example.simcareer.bean.DbManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegistrationPreferenciesFragment extends Fragment {

    private RegistrationPreferenciesViewModel mViewModel;
    TextInputLayout inputFavCircuit;
    TextInputLayout inputHatedCircuit;
    TextInputLayout inputFavCar;
    ImageView imageProPic;
    TextInputEditText inputFavNumber;
    MaterialButton buttonFinish;
    View root;
    Bundle bundle;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_registration_preferencies, container, false);
        imageProPic = root.findViewById(R.id.image_temp_pro_pic);
        inputFavNumber = root.findViewById(R.id.input_fav_number);
        inputFavCircuit = root.findViewById(R.id.text_input_fav_circuit);
        inputHatedCircuit = root.findViewById(R.id.text_input_hated_circuit);
        buttonFinish = root.findViewById(R.id.button_finish);
        inputFavCar = root.findViewById(R.id.text_input_fav_car);
        bundle = getArguments();
        imageProPic.setImageBitmap(DbManager.loadBitmapFromFiles(root.getContext(),"tmp.png"));
        try {
            List<String> circuits = DbManager.getCircuits(root.getContext());
            ((AutoCompleteTextView)inputFavCircuit.getEditText()).setAdapter(new ArrayAdapter<>(requireContext(), R.layout.list_item, circuits));
            ((AutoCompleteTextView)inputHatedCircuit.getEditText()).setAdapter(new ArrayAdapter<>(requireContext(), R.layout.list_item, circuits));
            List<String> cars = DbManager.getCars(root.getContext());
            ((AutoCompleteTextView)inputFavCar.getEditText()).setAdapter(new ArrayAdapter<>(requireContext(), R.layout.list_item, cars));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle = getArguments();
                Intent i = new Intent(requireActivity(), MainNavActivity.class);
                bundle = getExtraData(bundle);
                if(bundle == null)
                    Toast.makeText(root.getContext(), "Manca la scelta di alcune preferenze", Toast.LENGTH_SHORT).show();
                else {
                    int id = -1;
                    try {
                        id = DbManager.addUser(root.getContext(), bundle);
                        Bitmap tmp = DbManager.loadBitmapFromFiles(root.getContext(), "tmp.png");
                        String username = bundle.getString("username");
                        DbManager.saveBitmapToFiles(root.getContext(), tmp, username+id+".png");
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                    if(id != -1) {
                        i.putExtra("id", id);
                        startActivity(i);
                    }
                    else{
                        Toast.makeText(root.getContext(), "Qualcosa è andato storto", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RegistrationPreferenciesViewModel.class);
        // TODO: Use the ViewModel
    }

    public Bundle getExtraData(Bundle bundle){
        int favNumber = Integer.parseInt(inputFavNumber.getText().toString());
        String favCircuit = inputFavCircuit.getEditText().getText().toString();
        if(favCircuit.equals(getString(R.string.fav_circuit)))
            return null;
        String hatedCircuit = inputHatedCircuit.getEditText().getText().toString();
        if(hatedCircuit.equals(getString(R.string.hated_circuit)))
            return null;
        String favCar = inputFavCar.getEditText().getText().toString();
        if(favCar.equals(getString(R.string.fav_car)))
            return null;
        bundle.putInt("pilotNumber", favNumber);
        bundle.putString("favoriteCircuit", favCircuit);
        bundle.putString("hatedCircuit", hatedCircuit);
        bundle.putString("favoriteCar", favCar);
        return bundle;
    }
}