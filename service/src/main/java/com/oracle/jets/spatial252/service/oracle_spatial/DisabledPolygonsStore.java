package com.oracle.jets.spatial252.service.oracle_spatial;

import java.util.ArrayList;
import java.util.List;

import com.oracle.jets.spatial252.service.Polygon;

class DisabledPolygonsStore {

    private List<Polygon> polygons = new ArrayList<>();

    private static DisabledPolygonsStore instance = null;

    static DisabledPolygonsStore getInstance() {
        if (instance == null) {
            instance = new DisabledPolygonsStore();
        }
        return instance;
    }

    // uninstanciable
    private DisabledPolygonsStore() {}

    void add(Polygon polygon) {
        polygons.add(polygon);
    }

    // TODO getAll()はクローンしたオブジェクトを返却する
    List<Polygon> getAll() {
        return polygons;
    }

}
