package com.oracle.jets.spatial252.service.oracle_spatial;

import java.util.ArrayList;
import java.util.List;

import com.oracle.jets.spatial252.service.Polygon;

public class DisabledPolygonsCache {

    private List<Polygon> polygons = new ArrayList<Polygon>();

    private static DisabledPolygonsCache instance = null;

    static DisabledPolygonsCache getInstance() {
        if (instance == null) {
            instance = new DisabledPolygonsCache();
        }
        return instance;
    }

    void add(Polygon polygon) {
        polygons.add(polygon);
    }

    // TODO getAll()はクローンしたオブジェクトを返却する
    List<Polygon> getAll() {
        return polygons;
    }

}
