package com.voitu.ficoapp.model;

import java.util.Map;

public interface User {

    String getId();
    String getName();
    String getSurname();
    String getAlias();
    FicoPointsStatus getStatus();
    Role getRole();
    Map<String, Object> toJson();

    String getEmail();

    void setId(String id);

    void setStatus(FicoPointsStatus p);
}
