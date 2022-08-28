package com.voitu.ficoapp.model;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface DataSource {
    void getPoints(String user, Consumer<Optional<FicoPointsStatus>> action);

    void getUser(String id, Consumer<User> action);

    void  getUsers(Consumer<List<User>> action);
    void getToken(Consumer<String> action);
    void createUser(User u, Consumer<User> action);
    void getTags(Consumer<List<Tag>> tags);
    void updatePoints(UpdateInfo i, String selectedId, Consumer<UpdateInfo> o);
    void getVersion(Consumer<Long> update);
    void addTagWinner(Tag t, User u, Consumer<User> action);
    void addTag(String string, Consumer<String> action);
}
