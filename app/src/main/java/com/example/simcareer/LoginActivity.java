package com.example.simcareer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.simcareer.bean.DbManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText inputUsername;
    TextInputEditText inputPassword;
    ImageView imageLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DbManager.saveAllJSONToFiles(this);
        Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        Button buttonRegister = (Button) findViewById(R.id.buttonRegister);
        inputUsername = findViewById(R.id.input_login_username);
        inputPassword = findViewById(R.id.input_login_password);
        imageLogo = findViewById(R.id.image_app_logo);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, MainNavActivity.class);
                String username = inputUsername.getText().toString();
                String password = inputPassword.getText().toString();
                if(!username.isEmpty() && !password.isEmpty()){
                    try {
                        int id = DbManager.getUserId(LoginActivity.this, username, password);
                        if(id != -1) {
                            i.putExtra("id", id);
                            startActivity(i);
                        }
                        else{
                            TextInputLayout in = findViewById(R.id.inputUsername);
                            in.setError(" ");
                            TextInputLayout in2 = findViewById(R.id.inputPassword);
                            in2.setError("Credenziali errate");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    TextInputLayout in = findViewById(R.id.inputUsername);
                    in.setError(" ");
                    TextInputLayout in2 = findViewById(R.id.inputPassword);
                    in2.setError("Inserire credenziali");
                }
            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(i);
            }
        });
        imageLogo.setClickable(true);
        imageLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, MainNavActivity.class);
                i.putExtra("id", 0);
                startActivity(i);

            }
        });
    }
}