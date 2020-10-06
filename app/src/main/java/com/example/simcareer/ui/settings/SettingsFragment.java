package com.example.simcareer.ui.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.simcareer.R;
import com.example.simcareer.bean.DbManager;
import com.example.simcareer.bean.User;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;

public class SettingsFragment extends Fragment {

    TextInputLayout inputPsw;
    TextInputLayout inputNewPsw;
    TextInputLayout inputNewRepeatPsw;
    TextInputLayout inputNewEmail;
    TextInputLayout inputNewRepeatEmail;
    Button buttonUpdatePsw;
    Button buttonUpdateEmail;
    Button buttonDeleteUser;
    int id;
    User user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        inputPsw = root.findViewById(R.id.text_input_old_psw);
        inputNewPsw = root.findViewById(R.id.text_input_new_psw);
        inputNewRepeatPsw = root.findViewById(R.id.text_input_repeat_psw);
        inputNewEmail = root.findViewById(R.id.text_input_new_email);
        inputNewRepeatEmail = root.findViewById(R.id.text_input_repeat_new_email);
        buttonUpdatePsw = root.findViewById(R.id.button_update_psw);
        buttonUpdateEmail = root.findViewById(R.id.button_update_email);
        buttonDeleteUser = root.findViewById(R.id.button_delete_user);

        id = requireActivity().getIntent().getIntExtra("id", -1);
        try {
            user = DbManager.getFullUserById(requireContext(), id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        buttonUpdatePsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputPsw.getEditText().getText().toString().equals(user.getPassword())){
                    if(inputNewPsw.getEditText().getText().toString().equals(inputNewRepeatPsw.getEditText().getText().toString())){
                        user.setPassword(inputNewPsw.getEditText().getText().toString());
                        try {
                            boolean isUpdated = DbManager.updateUserPassword(requireContext(), user);
                            if(isUpdated)
                                Toast.makeText(requireContext(), "Password aggiornata!", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        inputNewPsw.setError("Password diverse");
                        inputNewRepeatPsw.setError(" ");
                    }
                }
                else{
                    inputPsw.setError("Vecchia password sbagliata");
                }

            }
        });

        buttonUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputNewEmail.getEditText().getText().toString();
                String remail = inputNewRepeatEmail.getEditText().getText().toString();
                if(email.equals(remail)){
                    user.setEmail(email);
                    try {
                        boolean isUpdated = DbManager.updateFullUser(requireContext(), user);
                        if(isUpdated)
                            Toast.makeText(requireContext(), "Email aggiornata!", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    inputNewEmail.setError("Email diverse");
                    inputNewRepeatEmail.setError(" ");
                }
            }
        });

        buttonDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
                builder.setTitle("Sei sicuro?");
                builder.setMessage("Questa azione rimuoverà il tuo account dall'app. Sicuro di voler procedere?");
                builder.setPositiveButton("Rimuovi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            DbManager.removeUser(requireContext(), id);
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
        });

        return root;
    }

    @Override
    public void onResume(){
        super.onResume();
        id = requireActivity().getIntent().getIntExtra("id", -1);
        try {
            user = DbManager.getFullUserById(requireContext(), id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}