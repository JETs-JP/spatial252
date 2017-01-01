package com.oracle.jets.spatial252;

import java.util.List;

interface GeometryService {

    Direction getShortestDirection(Point origin, Point destination)
            throws Spatial252ServiceException;

    List<RefugeWithDirection> getNearestRefuges(Point origin, int limit)
            throws Spatial252ServiceException;

    // TODO: disable
}
