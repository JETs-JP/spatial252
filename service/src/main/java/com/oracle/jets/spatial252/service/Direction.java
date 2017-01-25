package com.oracle.jets.spatial252.service;

import java.util.Collections;
import java.util.List;

public class Direction {

    private final List<Point> wayPoints;

    private final double cost;

    /** 
     * 
     * 
     * 本当は中継地点だけでコストが算出されるべきだが、その機能は
     * 位置情報サービスが持っていることが多いと予想されるので、
     * 中継地点とは独立したパラメータで受け取る。
     * 
     * @param wayPoints
     * @param cost
     */
    public Direction(List<Point> wayPoints, double cost) {
        // TODO: check parameters
        this.wayPoints = Collections.unmodifiableList(wayPoints);
        this.cost = cost;
    }

    public List<Point> getWayPoints() {
        return wayPoints;
    }

    public double getCost() {
        return cost;
    }

    public Point getOrigin() {
        return wayPoints.get(0);
    }

    public Point getDestination() {
        return wayPoints.get(wayPoints.size() - 1);
    }

}
