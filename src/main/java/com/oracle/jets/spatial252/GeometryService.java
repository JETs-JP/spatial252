package com.oracle.jets.spatial252;

import java.util.List;

interface GeometryService {

    Direction getShortestDirection(Point origin, Point destination);

    List<RefugeWithDirection> getNearestRefuges(Point origin, int limit);

    // TODO: disable
}
