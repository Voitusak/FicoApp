package com.voitu.ficoapp.model;

import androidx.annotation.NonNull;

import java.util.List;

public class TagImpl implements Tag {

    private final String id;
    private final String name;
    private final List<String> winnersIds;

    public TagImpl(String id, String name, List<String> winnersIds) {
        this.id = id;
        this.name = name;
        this.winnersIds = winnersIds;
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
    public List<String> getWinnersIds() {
        return winnersIds;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}
