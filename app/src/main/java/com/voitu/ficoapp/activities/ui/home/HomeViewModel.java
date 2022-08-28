package com.voitu.ficoapp.activities.ui.home;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.voitu.ficoapp.model.AuthManager;
import com.voitu.ficoapp.model.DataSource;
import com.voitu.ficoapp.model.DataSourceImpl;
import com.voitu.ficoapp.model.UpdateInfo;
import com.voitu.ficoapp.model.User;

import java.util.Collections;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<List<UpdateInfo>> status;
    private final AuthManager authManager;
    private final DataSource db;
    private User currentUser;
    private MutableLiveData<List<User>> users;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public HomeViewModel() {
        this.authManager = new AuthManager();
        this.db = new DataSourceImpl();
        this.status = new MutableLiveData<>(Collections.emptyList());
        this.users = new MutableLiveData<>(Collections.emptyList());
        this.db.getUser(this.authManager.getCurrentUser().getUid(), u->{
            this.currentUser = u;
            status.setValue(u.getStatus().getUpdates());
            this.updateData(u.getId());
        });
        this.db.getUsers(u -> {
            this.users.setValue(u);
        });
    }

    public LiveData<List<UpdateInfo>> getStatus() {
        return this.status;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateData(String id) {
        try {
            this.db.getPoints(id, p ->{
                p.ifPresent(v-> this.status.setValue(v.getUpdates()));
                System.out.println(p);
            });
        } catch (Exception e) {
            System.out.println("id -> " + id);
        }
    }

    public MutableLiveData<List<User>> getUsers() {
        return this.users;
    }

    public User getLoggedUser() {
        return this.currentUser;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addPoints(UpdateInfo i, String selectedId) {
        this.db.updatePoints(i, selectedId, u ->{
            this.updateData(selectedId);
        });
    }
}