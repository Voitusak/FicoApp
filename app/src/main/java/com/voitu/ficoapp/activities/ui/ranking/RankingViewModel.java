package com.voitu.ficoapp.activities.ui.ranking;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.voitu.ficoapp.model.AuthManager;
import com.voitu.ficoapp.model.DataSource;
import com.voitu.ficoapp.model.DataSourceImpl;
import com.voitu.ficoapp.model.Tag;
import com.voitu.ficoapp.model.User;

import java.util.Collections;
import java.util.List;

public class RankingViewModel extends ViewModel {

    private final MutableLiveData<List<Tag>> tags;
    private final AuthManager authManager;
    private final DataSource db;
    private final MutableLiveData<List<User>> users;

    public RankingViewModel() {
       tags = new MutableLiveData<>(Collections.emptyList());
       users = new MutableLiveData<>(Collections.emptyList());
       authManager = new AuthManager();
       db = new DataSourceImpl();
       updateData();
    }


    public void updateData(){
        this.db.getTags(tags::setValue);
        this.db.getUsers(this.users::setValue);
    }

    public MutableLiveData<List<Tag>> getTags(){
        return this.tags;
    }

    public MutableLiveData<List<User>> getUsers(){
        return this.users;

    }

}