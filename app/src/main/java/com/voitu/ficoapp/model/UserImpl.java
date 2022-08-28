package com.voitu.ficoapp.model;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserImpl implements User{

    private String id;
    private final String name;
    private final String surname;
    private final String alias;
    private FicoPointsStatus status;
    private final Role role;
    private final String email;

    public UserImpl(String id,String email, String name, String surname, String alias, FicoPointsStatus status, Role role) {
        this.name = name;
        this.surname = surname;
        this.alias = alias;
        this.status = status;
        this.role = role;
        this.id = id;
        this.email = email;
    }
    public UserImpl(String name, String email ,String surname, String alias, FicoPointsStatus status, Role role) {
        this("",email,name,surname,alias,status,role);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSurname() {
        return surname;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public FicoPointsStatus getStatus() {
        return status;
    }

    @Override
    public Role getRole() {
        return role;
    }

    @Override
    public Map<String, Object> toJson() {
        final Map<String, Object> m = new HashMap<>();
        m.put("name",this.name);
        m.put("surname",this.surname);
        m.put("alias",this.alias);
        m.put("role",this.role);
        m.put("email",this.email);
        m.put("threshold",this.status.getThreshold());
        m.put("updates",this.status.getUpdates());
        return m;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setStatus(FicoPointsStatus p) {
        this.status = p;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserImpl user = (UserImpl) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @NonNull
    @Override
    public String toString() {
        return this.alias.toUpperCase().charAt(0) + this.alias.toLowerCase().substring(1);
    }
}