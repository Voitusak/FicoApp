package com.example.ficoapp;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DataSource {

    private FirebaseFirestore db;
    private GraphicView view;



    public DataSource(GraphicView view){
        this.db = FirebaseFirestore.getInstance();
        this.view = view;
        this.db.collection("modifiche").addSnapshotListener((value, error) -> {
            if(!value.isEmpty()){
                value.getQuery().get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(String.valueOf(document.get("nome")).equals("tobia")){
                                view.notifiction((String.valueOf(document.get("valore"))));
                                break;
                            }
                        }
                    }
                });
            }
        });
    }




    public void update(String name){
        db.collection("punteggio")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                           if(String.valueOf(document.get("nome")).equals(name)){
                               view.updateView(name,
                                       Long.parseLong(String.valueOf(document.get("valore"))),
                                       Long.parseLong(String.valueOf(document.get("max"))));
                               break;
                           }
                        }
                    } else {
                        System.out.println("Error getting documents.");
                    }
                });
    }
}
