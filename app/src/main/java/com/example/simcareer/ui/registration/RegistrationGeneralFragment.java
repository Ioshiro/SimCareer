package com.example.simcareer.ui.registration;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.simcareer.R;
import com.example.simcareer.bean.DbManager;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class RegistrationGeneralFragment extends Fragment {

    private RegistrationGeneralViewModel mViewModel;
    private static final int PICK_IMAGE = 1;
    DatePickerDialog picker;
    EditText textDate;
    Button buttonNext;
    Button buttonPropic;
    ImageView imagePropic;
    View root;
    Uri imageUri;
    TextInputEditText inputUsername;
    TextInputEditText inputFullName;
    TextInputEditText inputAddress;
    TextInputEditText inputEmail;
    TextInputEditText inputRepeatEmail;
    TextInputEditText inputPassword;
    TextInputEditText inputRepeatPassword;

    public static RegistrationGeneralFragment newInstance() {
        return new RegistrationGeneralFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_registration_general, container, false);

        textDate=(EditText) root.findViewById(R.id.text_edit_date);
        imagePropic = root.findViewById(R.id.image_pro_pic);
        buttonNext = (Button) root.findViewById(R.id.button_next);
        buttonPropic = (Button) root.findViewById(R.id.button_change_pic);
        inputUsername = root.findViewById(R.id.input_username);
        inputFullName = root.findViewById(R.id.input_fullname);
        inputAddress = root.findViewById(R.id.input_address);
        inputEmail = root.findViewById(R.id.input_email);
        inputRepeatEmail = root.findViewById(R.id.input_repeat_email);
        inputPassword = root.findViewById(R.id.input_password);
        inputRepeatPassword = root.findViewById(R.id.input_repeat_password);

        buttonPropic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery, "Seleziona foto utente"), PICK_IMAGE);
            }
        });

        inputEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    if(!isValidEmail(inputEmail.getText())){
                        textDate.setError("Email non valida");
                    }
                }
            }
        });

        textDate.setInputType(InputType.TYPE_NULL);
        textDate.setClickable(true);
        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar tmp = Calendar.getInstance();
                                tmp.set(year, monthOfYear+1, dayOfMonth);
                                if(!tmp.after(Calendar.getInstance()))
                                    textDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                else{
                                    textDate.setError("Inserire data anteriore ad oggi");
                                }
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = getTextData();
                if(bundle != null) {
                    FragmentManager fm = getParentFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment, RegistrationPreferenciesFragment.class, bundle, "bundle");
                    fragmentTransaction.commit();
                }
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RegistrationGeneralViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent i) {
        super.onActivityResult(requestCode, resultCode, i);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && i != null) {
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


    public Bundle getTextData(){
        String username = inputUsername.getText().toString();
        if(username.isEmpty()){
            inputUsername.setError("Inserire username");
            return null;
        }

        String fullname = inputFullName.getText().toString();
        if(fullname.isEmpty()){
            inputFullName.setError("Inserire nome");
            return null;
        }

        String address = inputAddress.getText().toString();
        if(address.isEmpty()){
            inputAddress.setError("Inserire indirizzo");
            return null;
        }

        String dateofbirth = textDate.getText().toString();
        if(dateofbirth.isEmpty()){
            textDate.setError("Inserire data di nascità");
            return null;
        }

        String email = inputEmail.getText().toString();
        if(email.isEmpty()){
            inputEmail.setError("Inserire email");
            return null;
        }

        String remail = inputRepeatEmail.getText().toString();

        String psw = inputPassword.getText().toString();
        if(psw.isEmpty()){
            inputPassword.setError("Inserire password");
            return null;
        }

        String rpsw = inputRepeatPassword.getText().toString();
        if(!psw.equals(rpsw)){
            inputRepeatPassword.setError("");
            inputPassword.setError("Password diverse");
            return null;
        }

        if(!email.equals(remail)){
            inputRepeatEmail.setError("");
            inputEmail.setError("Email diverse");
            return null;
        }

        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("name", fullname);
        bundle.putString("localAddress", address);
        bundle.putString("dateOfBirth", dateofbirth);
        bundle.putString("email", email);
        bundle.putString("password", psw);
        return bundle;
    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
