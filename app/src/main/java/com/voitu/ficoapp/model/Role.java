package com.voitu.ficoapp.model;

public enum Role {
    MEMBER("Membro ufficiale", android.R.color.holo_green_light,10),
    GUEST("Ospite canonico", android.R.color.darker_gray,1),
    PIU_TOBIA("Piú Tobia", android.R.color.holo_purple,2),
    ENEMY("Nemico ufficiale", android.R.color.holo_red_light,0);

    private final String name;
    private final int color;
    private final int permissionLevel;

    private Role(String name, int color,int p) {
        this.name = name;
        this.color = color;
        this.permissionLevel = p;
    }

    public int getPermissionLevel() {
        return permissionLevel;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }
}
