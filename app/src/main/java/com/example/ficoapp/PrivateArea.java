package com.example.ficoapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Pair;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PrivateArea extends AppCompatActivity {

    public static final String PATTERN = "dd/MM/yyyy HH:mm";
    private BaseAdapter adapter;
    private List<Pair<String,String>> list;
    private String name;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_area);
        name = "tobia";
        ListView listView = findViewById(R.id.listView);

        list = new ArrayList<>();


        adapter = new myAdapter(getApplicationContext(),  list);

        // Here, you set the data in your ListView
        listView.setAdapter(adapter);
        populateList();

        Button b = findViewById(R.id.addPoints);
        EditText box = findViewById(R.id.pointsbox);
        b.setOnClickListener(view -> {
            if(!box.getText().toString().isEmpty()){
                addPoints(box.getText().toString());
            }
        });

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.privaterefresh);
        pullToRefresh.setOnRefreshListener(() -> {
            populateList(); // your code
            pullToRefresh.setRefreshing(false);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addPoints(String toString) {
        HashMap<String,Object> h = new HashMap<>();
        h.put("data", Timestamp.now());
        h.put("valore", toString);
        try{
            Long.parseLong(toString);
        } catch (Exception e){
            return;
        }
        h.put("nome",name);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("modifiche")
                .add(h)
                .addOnSuccessListener((OnSuccessListener<DocumentReference>) documentReference -> populateList())
                .addOnFailureListener((OnFailureListener) e -> System.out.println("Insert error"));

        db.collection("punteggio")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(String.valueOf(document.get("nome")).equals(name) ){
                                long actual = Long.parseLong(String.valueOf(document.get("valore")));
                                long added =  Long.parseLong(toString);
                                long newValue = actual + added;
                                if ( actual + added <= 0) {
                                    newValue = 0;
                                }
                                db.collection("punteggio")
                                        .document("1")
                                        .update("valore", newValue);
                            }
                        }
                    } else {
                        System.out.println("Error getting documents.");
                    }
                });

        ((EditText)findViewById(R.id.pointsbox)).setText("");

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void populateList() {
        list.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("modifiche")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(String.valueOf(document.get("nome")).equals(name) ){
                                Pair<String,String> p = new Pair<>(getDate(((Timestamp)document.get("data")).getSeconds())
                                        ,String.valueOf(document.get("valore")));
                                if(!list.contains(p)){
                                    list.add(p);
                                }
                            }
                        }
                        sortList();
                    } else {
                        System.out.println("Error getting documents.");
                    }
                });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortList() {
        SimpleDateFormat format = new SimpleDateFormat(PATTERN);
        list.sort((a,b) -> {
            try {
                return format.parse(b.first).compareTo(format.parse(a.first));
            } catch (ParseException e) {
                System.out.println(e);
                return 1;
            }
        });
        adapter.notifyDataSetChanged();
    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format(PATTERN, cal).toString();
        return date;
    }
}