package com.voitu.ficoapp.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UpdateInfoImpl implements UpdateInfo{
    private final String user;
    private final Date date;
    private final Long points;

    public UpdateInfoImpl(Date date, Long points, String user) {
        this.user = user;
        this.date = date;
        this.points = points;
    }

    @Override
    public String getActionUser() {
        return this.user;
    }

    @Override
    public Date getDate() {
        return this.date;
    }

    @Override
    public Long getPoints() {
        return this.points;
    }

    @Override
    public Map<String, Object> toJson() {
        final Map<String, Object> m = new HashMap<>();
        m.put("made_by",this.user);
        m.put("value",this.points);
        m.put("date",this.date);
        return m;
    }
}
