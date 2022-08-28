package com.voitu.ficoapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.voitu.ficoapp.databinding.AddRankActivityBinding;

public class AddRankActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private AddRankActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = AddRankActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}