package com.oracle.jets.spatial252.searcher;

import oracle.spatial.geometry.JGeometry;

public class Node extends SpatialObject {

    Node(long id, JGeometry origin) {
        super(id, origin);
    }

    Node(long id, JGeometry origin, double distance) {
        super(id, origin, distance);
    }

}
