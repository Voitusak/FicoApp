package com.voitu.ficoapp.model;


import static android.content.ContentValues.TAG;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DataSourceImpl implements DataSource {

    public static final String SCORE = "punteggio";
    private static final String USERS = "users";
    private static final String INFO = "info";
    private static final String TAGS = "etichette";
    private final FirebaseFirestore db;


    public DataSourceImpl() {
        this.db = FirebaseFirestore.getInstance();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void getPoints(String user, Consumer<Optional<FicoPointsStatus>> action) {
        DocumentReference docRef = db.collection(USERS)
                .document(user);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                assert document != null;
                if (document.exists()) {
                    Map<String, Object> d = document.getData();
                    try {
                        List<UpdateInfo> updates = getUpdates(d);
                        long threshold = (Long) d.get("threshold");
                        Optional<FicoPointsStatus> v = Optional.
                                of(new FicoPointsStatusImpl(updates, threshold));
                        action.accept(v);
                    } catch (Exception e) {
                        Log.e(TAG, "Conversion error," + e);
                    }
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void getUser(String id, Consumer<User> action) {
        DocumentReference docRef = db.collection(USERS).document(id);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                assert document != null;
                if (document.exists()) {
                    Map<String, Object> d = document.getData();
                    try {
                        assert d != null;
                        User u = dataToUser(d);
                        u.setId(id);
                        action.accept(u);
                    } catch (Exception e) {
                        Log.e(TAG, "Conversion error," + e);
                    }
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    private User dataToUser(Map<String, Object> d) {
        List<UpdateInfo> updates = getUpdates(d);
        long threshold = (Long) d.get("threshold");
        Optional<FicoPointsStatus> v = Optional.
                of(new FicoPointsStatusImpl(updates, threshold));
        User u = new UserImpl((String) d.get("name"),(String) d.get("email"),
                (String) d.get("surname"),
                (String) d.get("alias"),v.orElse(null),
                Role.valueOf((String) d.get("role")));
        return u;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    private List<UpdateInfo> getUpdates(Map<String, Object> d) {
        return ((List<Map<String, Object>>)
                Objects.requireNonNull(d.get("updates")))
                .stream().map(a -> new UpdateInfoImpl(((Timestamp)
                        Objects.requireNonNull(a.get("date"))).toDate(),
                        (Long) a.get("value"),
                        (String)a.get("made_by")))
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void getUsers(Consumer<List<User>> action) {
        db.collection(USERS)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            assert document != null;
                            if (document.exists()) {
                                Map<String, Object> d = document.getData();
                                try {
                                   User u = dataToUser(d);
                                   u.setId(document.getId());
                                   users.add(u);
                                } catch (Exception e) {
                                    Log.e(TAG, "Conversion error users," + e);
                                }
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        }
                        action.accept(users);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void getToken(Consumer<String> action) {
        DocumentReference docRef = db.collection(INFO)
                .document("private");
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                assert document != null;
                if (document.exists()) {
                    Map<String, Object> d = document.getData();
                    try {
                        assert d != null;
                        String token = (String) d.get("token");
                        action.accept(token);
                    } catch (Exception e) {
                        Log.e(TAG, "Conversion error," + e);
                    }
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void createUser(User u, Consumer<User> action) {
        db.collection("users").document(u.getId())
                .set(u.toJson())
                .addOnSuccessListener(aVoid -> action.accept(u))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void getTags(Consumer<List<Tag>> action) {
        db.collection(TAGS)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Tag> tags = new ArrayList<>();
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            assert document != null;
                            if (document.exists()) {
                                Map<String, Object> d = document.getData();
                                try {
                                    String id = (String) document.getId();
                                    String name = (String) d.get("name");
                                    List<String> winnersIds = (List<String>) d.get("winners");
                                    tags.add(new TagImpl(id,name,winnersIds));
                                } catch (Exception e) {
                                    Log.e(TAG, "Conversion error tag," + e);

                                }
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        }
                        action.accept(tags);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void updatePoints(UpdateInfo i, String selectedId, Consumer<UpdateInfo> o) {
        DocumentReference document = db.collection(USERS).document(selectedId);
        document
                .update("updates",  FieldValue.arrayUnion(i.toJson()))
                .addOnSuccessListener(aVoid -> o.accept(i))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void getVersion(Consumer<Long> update) {
        DocumentReference docRef = db.collection(INFO)
                .document("version");
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                assert document != null;
                if (document.exists()) {
                    Map<String, Object> d = document.getData();
                    try {
                        assert d != null;
                        Long token = (Long) d.get("num");
                        update.accept(token);
                    } catch (Exception e) {
                        Log.e(TAG, "Conversion error," + e);
                    }
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void addTagWinner(Tag t, User u, Consumer<User> action) {
        List<String> list = t.getWinnersIds();
        list.add(u.getId());
        DocumentReference document = db.collection(TAGS)
                .document(t.getId());
        document
                .update("winners",  list)
                .addOnSuccessListener(aVoid -> action.accept(u))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void addTag(String string, Consumer<String> action) {
        HashMap<String,Object> v = new HashMap<>();
        v.put("name", string);
        v.put("winners", Collections.emptyList());
        db.collection(TAGS)
                .document()
                .set(v)
                .addOnSuccessListener(aVoid -> action.accept(string))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
    }
}
