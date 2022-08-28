package com.voitu.ficoapp.model;

import java.util.List;

public interface FicoPointsStatus {
    long getPoints();
    long getThreshold();
    List<UpdateInfo> getUpdates();
}
