package com.voitu.ficoapp.model;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.voitu.ficoapp.controller.Common;

import java.util.function.Consumer;

public class AuthManager {

    private final FirebaseAuth auth;

    public AuthManager() {
        this.auth = FirebaseAuth.getInstance();
    }

    public void logOut() {
        this.auth.signOut();
    }


    public boolean isLoggedIn() {
        return this.getCurrentUser() != null;
    }

    public FirebaseUser getCurrentUser(){
        return auth.getCurrentUser();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void createAccount(User u, String password, Consumer<User> action){
        this.auth.createUserWithEmailAndPassword(u.getEmail(),password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = auth.getCurrentUser();
                        assert user != null;
                        String id = user.getUid();
                        u.setId(id);
                        DataSource db = new DataSourceImpl();
                        db.createUser(u, action);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    }
                });
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void login(String email, String password, Context c, Consumer<FirebaseUser> action){
        this.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = this.auth.getCurrentUser();
                        action.accept(user);
                    } else {
                        Common.showError("auth failed",c);
                    }
                });
    }
}
