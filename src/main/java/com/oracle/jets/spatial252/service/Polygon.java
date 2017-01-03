package com.oracle.jets.spatial252.service;

import java.util.List;

public class Polygon {

    private final List<Point> vertices;

    public Polygon(List<Point> vertices) {
        this.vertices = vertices;
    }

    public List<Point> getVertices() {
        return vertices;
    }

}
