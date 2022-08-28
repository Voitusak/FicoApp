package com.voitu.ficoapp.activities;

import static com.voitu.ficoapp.activities.RegisterActivity.MISSING_FIELD_ERROR;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.voitu.ficoapp.R;
import com.voitu.ficoapp.controller.Common;
import com.voitu.ficoapp.model.AuthManager;

public class LoginActivity extends AppCompatActivity {

    private AuthManager auth;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.auth = new AuthManager();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onStart() {
        super.onStart();
        //this.auth.logOut();
        // Check if user is signed in (non-null) and update UI accordingly.
        if(this.auth.isLoggedIn()){
            Common.startMainActivity(this);
            finish();
        } else {
            login();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void login() {
        setContentView(R.layout.login_activity);
        Button button = findViewById(R.id.loginButton);
        Button register = findViewById(R.id.loginRegisterButton);
        EditText email = findViewById(R.id.loginEmail);
        EditText password = findViewById(R.id.loginPassword);
        button.setOnClickListener(view -> {
            if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                Common.showError(MISSING_FIELD_ERROR, this);
                return;
            }
            this.auth.login(email.getText().toString(),
                    password.getText().toString(),
                    this, u -> {
                        Common.startMainActivity(this);
                    });
        });
        register.setOnClickListener(view -> {
            Common.startRegisterActivity(this);
        });
    }
}
