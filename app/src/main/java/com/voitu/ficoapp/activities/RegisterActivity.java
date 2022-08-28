package com.voitu.ficoapp.activities;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

import com.voitu.ficoapp.R;
import com.voitu.ficoapp.controller.Common;
import com.voitu.ficoapp.model.AuthManager;
import com.voitu.ficoapp.model.DataSource;
import com.voitu.ficoapp.model.DataSourceImpl;
import com.voitu.ficoapp.model.User;
import com.voitu.ficoapp.model.UserFactory;
import com.voitu.ficoapp.model.UserFactoryImpl;

public class RegisterActivity extends Activity {


    private static final int PSW_LENGTH = 6;
    public static final String MISSING_FIELD_ERROR = "Compila tutti i campi";
    public static final String SHORT_PSW_ERROR = "Password troppo corta";
    public static final String DIFFERENT_PSW_ERROR = "Le password non corrispondono";
    public static final String TOKEN_ERROR = "Token errato";
    private AuthManager auth;
    private String token;
    private UserFactory factory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.auth = new AuthManager();
        DataSource d = new DataSourceImpl();
        d.getToken(t -> this.token = t);
        this.factory = new UserFactoryImpl();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onStart() {
        super.onStart();
        //this.auth.logOut();
        // Check if user is signed in (non-null) and update UI accordingly.
        if(this.auth.isLoggedIn()){
            reload();
        } else {
            register();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void register() {
        setContentView(R.layout.register_activity);
        Button button = findViewById(R.id.registerButton);
        Button login = findViewById(R.id.registerLoginButton);
        login.setOnClickListener(v -> {
            Common.startLoginActivity(this);
            finish();
        });
        EditText name = findViewById(R.id.name);
        EditText surname = findViewById(R.id.surname);
        EditText email = findViewById(R.id.registerEmail);
        EditText alias = findViewById(R.id.alias);
        EditText password = findViewById(R.id.registerPassword);
        EditText confirm = findViewById(R.id.password2);

        EditText token = findViewById(R.id.token);
        button.setOnClickListener(view -> {
            if(name.getText().toString().isEmpty() || surname.getText().toString().isEmpty()
            || email.getText().toString().isEmpty() || password.getText().toString().isEmpty()
                || alias.getText().toString().isEmpty() || confirm.getText().toString().isEmpty()){
                Common.showError(MISSING_FIELD_ERROR, this);
                return;
            }
            if(!token.getText().toString().isEmpty() && !token.getText().toString().equals(this.token)){
                Common.showError(TOKEN_ERROR, this);
                return;
            }
            if (password.getText().toString().length() < PSW_LENGTH){
                Common.showError(SHORT_PSW_ERROR, this);
                return;
            }
            if(!password.getText().toString().equals(confirm.getText().toString())){
                Common.showError(DIFFERENT_PSW_ERROR,this);
                return;
            }
            createAccount(email.getText().toString(), password.getText().toString(),
                    name.getText().toString(), surname.getText().toString(),
                    alias.getText().toString(),token.getText().toString());
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createAccount(String email, String password, String name, String surname, String alias, String s) {
        User user;
        if(s.equals(this.token)){
            user = this.factory.createFicoMember(name,email,surname,alias);
        } else {
            user = this.factory.createFicoGuest(name,email,surname,alias);
        }
        this.auth.createAccount(user, password, u ->{
            reload();
        });
    }



    private void reload() {
        Common.startMainActivity(this);
        finish();
    }
}
