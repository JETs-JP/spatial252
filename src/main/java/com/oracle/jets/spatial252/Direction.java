package com.oracle.jets.spatial252;

import java.util.LinkedList;
import java.util.List;

class Direction {

    private final List<Point> WayPoints = new LinkedList<Point>();

    public List<Point> getWayPoints() {
        return WayPoints;
    }

    void AddWayPoint(Point point) {
        WayPoints.add(point);
    }

}
