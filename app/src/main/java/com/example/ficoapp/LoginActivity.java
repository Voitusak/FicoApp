package com.example.ficoapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

public class LoginActivity extends AppCompatActivity {

    private String pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        getPin();
        OtpView otpView;
        otpView = findViewById(R.id.otp_view);
        otpView.setOtpCompletionListener((OnOtpCompletionListener) otp -> {
            if (otp.equals(pin)){
                    Intent intent = new Intent(LoginActivity.this, PrivateArea.class);
                    startActivity(intent);
                    finish();
            }
            else
                otpView.setText("");

        });
    }

    private void getPin() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("info")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(String.valueOf(document.get("nome")).equals("pin")){
                               this.pin = String.valueOf(document.get("valore"));
                               break;
                            }
                        }
                    } else {
                        System.out.println("Error getting documents.");
                    }
                });
    }
}
