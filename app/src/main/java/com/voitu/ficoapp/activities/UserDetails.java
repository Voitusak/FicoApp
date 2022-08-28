package com.voitu.ficoapp.activities;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.voitu.ficoapp.R;
import com.voitu.ficoapp.databinding.ActivityUserDetailsBinding;
import com.voitu.ficoapp.model.AuthManager;
import com.voitu.ficoapp.model.DataSource;
import com.voitu.ficoapp.model.DataSourceImpl;

public class UserDetails extends AppCompatActivity {

    private ActivityUserDetailsBinding binding;
    private final Long version = 3L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AuthManager authManager = new AuthManager();
        DataSource dataSource = new DataSourceImpl();
        //authManager.logOut();

        if (!authManager.isLoggedIn()){
            Intent intent = new Intent(UserDetails.this, RegisterActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        binding = ActivityUserDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_account,
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_main, R.id.navigation_leaderboard)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_user_details);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        dataSource.getVersion(v-> {
            if(!v.equals(version)){
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Obsolete version")
                        .setMessage("update the app")
                        .setPositiveButton("Yes", (dialogInterface, i) -> {
                            finish();
                        })
                        .setNegativeButton("No", (dialogInterface, i) ->
                        {
                            finish();
                        })
                        .show();
            }
        });

    }
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

}