package com.voitu.ficoapp.model;

import java.util.Date;
import java.util.Map;

public interface UpdateInfo {
    String getActionUser();
    Date getDate();
    Long getPoints();

    Map<String,Object> toJson();
}
