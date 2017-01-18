package com.oracle.jets.spatial252.service;

import java.util.List;

public class Polygon {

    private final List<Point> coordinates;

    public Polygon(List<Point> coordinates) {
        this.coordinates = coordinates;
    }

    public List<Point> getCoordinates() {
        return coordinates;
    }

}
