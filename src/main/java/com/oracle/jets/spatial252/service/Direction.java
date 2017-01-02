package com.oracle.jets.spatial252.service;

import java.util.LinkedList;
import java.util.List;

public class Direction {

    private final List<Point> WayPoints = new LinkedList<Point>();

    public List<Point> getWayPoints() {
        return WayPoints;
    }

    public void AddWayPoint(Point point) {
        WayPoints.add(point);
    }

}
