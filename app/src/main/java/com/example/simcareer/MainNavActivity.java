package com.example.simcareer;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simcareer.bean.DbManager;
import com.example.simcareer.bean.User;
import com.example.simcareer.ui.cup.CupFragment;
import com.example.simcareer.ui.cup.CupFragmentDirections;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;

public class MainNavActivity extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    TextView textSideName;
    int userID;
    static MenuItem checkOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        textSideName = findViewById(R.id.text_nav_header_name);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView =(NavigationView)  findViewById(R.id.nav_view);
        navigationView.bringToFront();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_settings, R.id.nav_leaderboard, R.id.nav_profile, R.id.nav_subscription)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public void onStart(){
        super.onStart();
        Intent i = getIntent();
        userID = i.getIntExtra("id", -1);

        try {
            User user = DbManager.getFullUserById(this, userID);
            if(user == null)
                Log.d("NavOnStart", "user not found at id "+userID);
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.bringToFront();
            View hView =  navigationView.getHeaderView(0);
            TextView nav_user = (TextView)hView.findViewById(R.id.text_nav_header_name);
            TextView nav_email = (TextView)hView.findViewById(R.id.text_nav_header_team);
            ImageView nav_pic = (ImageView)hView.findViewById(R.id.image_nav_header_pic);
            nav_user.setText(user.getName());
            nav_email.setText(user.getEmail());
            if(!user.getPicName().isEmpty()){
                nav_pic.setImageBitmap(DbManager.loadBitmapFromFiles(this, user.getPicName()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar_menu, menu);
        checkOut = (MenuItem) menu.findItem(R.id.action_nav_cup_to_nav_leaderboard);
        checkOut.setVisible(false);
        return true;
    }

    public static void hideIcon(){
        checkOut.setVisible(false);
    }

    public static void showIcon(){

        checkOut.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_nav_cup_to_nav_leaderboard:
                CupFragment.navigateToLeaderboards();
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}