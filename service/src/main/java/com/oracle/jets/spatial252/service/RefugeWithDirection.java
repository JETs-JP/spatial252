package com.oracle.jets.spatial252.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oracle.jets.spatial252.service.oracle_spatial.searcher.Refuge;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
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

    public boolean getEarthquake() {
        return refuge.getEarthquake() >= 1 ? true : false;
    }

    public boolean getTsunami() {
        return refuge.getTsunami() >= 1 ? true : false;
    }

    public boolean getFlood() {
        return refuge.getFlood() >= 1 ? true : false;
    }

    public boolean getVolcanic() {
        return refuge.getVolcanic() >= 1 ? true : false;
    }

    public boolean getOtherHazard() {
        return refuge.getOtherHazard() >= 1 ? true : false;
    }

    public boolean getNotDefined() {
        return refuge.getNotDefined() >= 1 ? true : false;
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
