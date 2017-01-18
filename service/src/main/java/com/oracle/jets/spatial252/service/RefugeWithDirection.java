package com.oracle.jets.spatial252.service;

import com.oracle.jets.spatial252.searcher.Refuge;

public class RefugeWithDirection {

    private final Refuge refuge;

    private final Direction direction;

    public RefugeWithDirection(Refuge refuge, Direction direction) {
        this.refuge = refuge;
        this.direction = direction;
    }

    public long getId() {
        return refuge.getId();
    }

    public String getArea() {
        return refuge.getArea();
    }

    public String getName() {
        return refuge.getName();
    }

    public String getAddress() {
        return refuge.getAddress();
    }

    public String getType() {
        return refuge.getType();
    }

    public int getCapacity() {
        return refuge.getCapacity();
    }

    public String getScale() {
        return refuge.getScale();
    }

    public int getEarthquake() {
        return refuge.getEarthquake();
    }

    public int getTsunami() {
        return refuge.getTsunami();
    }

    public int getFlood() {
        return refuge.getFlood();
    }

    public int getVolcanic() {
        return refuge.getVolcanic();
    }

    public int getOtherHazard() {
        return refuge.getOtherHazard();
    }

    public int getNotDefined() {
        return refuge.getNotDefined();
    }

    public int getLevel() {
        return refuge.getLevel();
    }

    public String getRemarks() {
        return refuge.getRemarks();
    }

    public Point getLocation() {
        double[] point = refuge.getLocation().getPoint();
        return new Point(point[1], point[0]);
    }

    public Direction getDirection() {
        return direction;
    }

}
