package com.voitu.ficoapp.activities.ui.account;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;

import com.voitu.ficoapp.model.AuthManager;
import com.voitu.ficoapp.model.DataSource;
import com.voitu.ficoapp.model.DataSourceImpl;
import com.voitu.ficoapp.model.User;

import java.util.function.Consumer;

public class AccountViewModel extends ViewModel {

    private  User currentUser;
    private final DataSource db;
    private final AuthManager auth;

    public AccountViewModel() {
        db = new DataSourceImpl();
        auth = new AuthManager();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getUser(Consumer<User> action) {
        this.db.getUser(this.auth.getCurrentUser().getUid(), action);
    }

}