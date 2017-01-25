package com.oracle.jets.spatial252.service.oracle_spatial.searcher;

import oracle.spatial.geometry.JGeometry;

public class Link extends SpatialObject {

    Link(long id, JGeometry origin) {
        super(id, origin);
    }

    Link(long id, JGeometry origin, double distance) {
        super(id, origin, distance);
    }

}
