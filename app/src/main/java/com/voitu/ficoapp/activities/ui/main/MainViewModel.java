package com.voitu.ficoapp.activities.ui.main;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.voitu.ficoapp.model.AuthManager;
import com.voitu.ficoapp.model.DataSource;
import com.voitu.ficoapp.model.DataSourceImpl;
import com.voitu.ficoapp.model.FicoPointsStatus;
import com.voitu.ficoapp.model.User;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<FicoPointsStatus> status;
    private final AuthManager authManager;
    private final DataSource db;
    private User currentUser;

    public MainViewModel() {
        this.authManager = new AuthManager();
        this.db = new DataSourceImpl();
        this.status = new MutableLiveData<>();
        this.db.getUser(this.authManager.getCurrentUser().getUid(), u->{
            this.currentUser = u;
             status.setValue(u.getStatus());
        });
    }

    public LiveData<FicoPointsStatus> getStatus() {
        return this.status;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateData() {
        try {
            this.db.getPoints(currentUser.getId(), p ->{
                p.ifPresent(this.status::setValue);
            });
        } catch (Exception e) {
            //
        }
    }
}