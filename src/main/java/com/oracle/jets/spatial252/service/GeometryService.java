package com.oracle.jets.spatial252.service;

import java.util.List;

public interface GeometryService {

    Direction getShortestDirection(Point origin, Point destination)
            throws Spatial252ServiceException;

    List<RefugeWithDirection> getNearestRefuges(Point origin, int limit)
            throws Spatial252ServiceException;

    // TODO: disable
}
