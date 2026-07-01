package com.cctv.models;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

public class Camera {

    private final String name;
    private Location location;
    private ArmorStand standEntity; // visual representation, may be null after reload until re-linked

    public Camera(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ArmorStand getStandEntity() {
        return standEntity;
    }

    public void setStandEntity(ArmorStand standEntity) {
        this.standEntity = standEntity;
    }
}
