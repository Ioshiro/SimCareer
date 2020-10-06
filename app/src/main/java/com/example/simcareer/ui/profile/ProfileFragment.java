package com.example.simcareer.ui.profile;

import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simcareer.R;
import com.example.simcareer.bean.DbManager;
import com.example.simcareer.bean.User;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    TextInputLayout inputName;
    TextInputLayout inputAddress;
    TextInputLayout inputDate;
    TextInputLayout inputFavNum;
    TextInputLayout inputFavCar;
    TextInputLayout inputFavCircuit;
    TextInputLayout inputHatedCircuit;
    ImageView imagePropic;
    View root;
    Button buttonChange;
    Button buttonUpdate;
    Uri imageUri;
    User user;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_profile, container, false);
        inputName = root.findViewById(R.id.text_input_name);
        inputAddress = root.findViewById(R.id.text_input_address);
        inputDate = root.findViewById(R.id.text_input_dob);
        inputFavNum = root.findViewById(R.id.text_input_fav_number);
        inputFavCar = root.findViewById(R.id.text_input_fav_car);
        inputFavCircuit = root.findViewById(R.id.text_input_fav_circuit);
        inputHatedCircuit = root.findViewById(R.id.text_input_hated_circuit);
        imagePropic = root.findViewById(R.id.image_pro_pic);
        buttonChange = root.findViewById(R.id.button_change);
        buttonUpdate = root.findViewById(R.id.button_update);
        int id = requireActivity().getIntent().getIntExtra("id", -1);
        try {
            user = DbManager.getFullUserById(requireContext(), id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        inputName.getEditText().setText(user.getName());
        inputAddress.getEditText().setText(user.getLocalAddress());
        inputDate.getEditText().setText(user.getDateOfBirth());
        inputFavNum.getEditText().setText(String.valueOf(user.getPilotNumber()));
        inputFavCircuit.getEditText().setText(user.getFavoriteCircuit());
        inputFavCar.getEditText().setText(user.getFavoriteCar());
        inputHatedCircuit.getEditText().setText(user.getHatedCircuit());

        inputDate.getEditText().setInputType(InputType.TYPE_NULL);
        inputDate.getEditText().setClickable(true);
        inputDate.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                inputDate.getEditText().setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });


        imagePropic.setImageBitmap(DbManager.loadBitmapFromFiles(root.getContext(),user.getPicName()));
        buttonChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery, "Seleziona foto utente"), 1);
            }
        });


        try {
            List<String> circuits = DbManager.getCircuits(root.getContext());
            ((AutoCompleteTextView)inputFavCircuit.getEditText()).setAdapter(new ArrayAdapter<>(requireContext(), R.layout.list_item, circuits));
            ((AutoCompleteTextView)inputHatedCircuit.getEditText()).setAdapter(new ArrayAdapter<>(requireContext(), R.layout.list_item, circuits));
            List<String> cars = DbManager.getCars(root.getContext());
            ((AutoCompleteTextView)inputFavCar.getEditText()).setAdapter(new ArrayAdapter<>(requireContext(), R.layout.list_item, cars));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setName(inputName.getEditText().getText().toString());
                user.setLocalAddress(inputAddress.getEditText().getText().toString());
                user.setDateOfBirth(inputDate.getEditText().getText().toString());
                user.setPilotNumber(Integer.parseInt(inputFavNum.getEditText().getText().toString()));
                user.setFavoriteCar(inputFavCar.getEditText().getText().toString());
                user.setFavoriteCircuit(inputFavCircuit.getEditText().getText().toString());
                user.setHatedCircuit(inputHatedCircuit.getEditText().getText().toString());
                Bitmap tmp = DbManager.loadBitmapFromFiles(requireContext(), "tmp.png");
                try {
                    DbManager.saveBitmapToFiles(requireContext(), tmp, user.getUsername()+user.getId()+".png");
                    boolean isUpdated = DbManager.updateFullUser(requireContext(), user);
                    NavigationView navigationView = (NavigationView) requireActivity().findViewById(R.id.nav_view);
                    View hView =  navigationView.getHeaderView(0);
                    TextView nav_user = (TextView)hView.findViewById(R.id.text_nav_header_name);
                    ImageView nav_pic = (ImageView)hView.findViewById(R.id.image_nav_header_pic);
                    nav_user.setText(user.getName());
                    if(!user.getPicName().isEmpty()){
                        nav_pic.setImageBitmap(DbManager.loadBitmapFromFiles(requireContext(), user.getUsername()+user.getId()+".png"));
                    }
                    if(isUpdated)
                        Toast.makeText(requireContext(), "Utente aggiornato!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(requireContext(), "C'è stato qualche errore", Toast.LENGTH_SHORT).show();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent i) {
        super.onActivityResult(requestCode, resultCode, i);
        if (requestCode == 1 && resultCode == RESULT_OK && i != null) {
            imageUri = i.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                DbManager.saveBitmapToFiles(root.getContext(), bitmap, "tmp.png");
                imagePropic.setImageBitmap(bitmap);
                Log.d("uri",imageUri.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}