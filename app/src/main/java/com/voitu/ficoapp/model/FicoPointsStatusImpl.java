package com.voitu.ficoapp.model;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class FicoPointsStatusImpl implements FicoPointsStatus {
    private final long threshold;
    private final List<UpdateInfo> updates;

    public FicoPointsStatusImpl(List<UpdateInfo> updates, long threshold) {
        this.updates = updates;
        this.threshold = threshold;
    }

    public FicoPointsStatusImpl() {
        this(new ArrayList<>(),0);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public long getPoints() { return this.updates.stream().mapToLong(UpdateInfo::getPoints).sum(); }
    @Override
    public long getThreshold() {
        return threshold;
    }

    @Override
    public List<UpdateInfo> getUpdates() {
        return this.updates;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public String toString() {
        return "FicoPointsStatusImpl{" +
                "points=" + getPoints() +
                ", threshold=" + threshold +
                '}';
    }
}